async function sendMessage() {

    const input = document.getElementById("message");
    const message = input.value.trim();

    if (message === "") {
        return;
    }

    const chatBox = document.getElementById("chatBox");

    // User Message
    chatBox.innerHTML +=
        `<div class="user">
            <span>${message}</span>
        </div>`;

    input.value = "";

    chatBox.scrollTop = chatBox.scrollHeight;

    try {

		const response = await fetch(`/stream?message=${encodeURIComponent(message)}`);

		const reader = response.body.getReader();
		const decoder = new TextDecoder();

		const aiDiv = document.createElement("div");
		aiDiv.className = "ai";

		const aiSpan = document.createElement("span");
		aiDiv.appendChild(aiSpan);

		chatBox.appendChild(aiDiv);

		let fullResponse = "";

		while (true) {

		    const { done, value } = await reader.read();

		    if (done) break;

		    let chunk = decoder.decode(value, { stream: true });

		    // Remove SSE prefix
		    chunk = chunk.replace(/data:\s*/g, "");

		    // Remove empty lines
		    chunk = chunk.replace(/\n\n/g, "");

		    fullResponse += chunk;

		    aiSpan.innerHTML = marked.parse(fullResponse);

		    chatBox.scrollTop = chatBox.scrollHeight;
		}

        chatBox.scrollTop = chatBox.scrollHeight;

    } catch (error) {

        chatBox.innerHTML +=
            `<div class="ai">
                <span style="color:red;">
                    Failed to connect to AI.
                </span>
            </div>`;
    }
	
	
}

document.getElementById("message").addEventListener("keydown", function(event) {
	    console.log(event.key);

	    if (event.key === "Enter") {
	        event.preventDefault();
	        sendMessage();
	    }
	});