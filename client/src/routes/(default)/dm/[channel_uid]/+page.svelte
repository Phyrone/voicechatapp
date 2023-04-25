<script lang="ts">
    import type {PageData} from "./$types"

    export let data: PageData;

    let test_messages: string[] = [];

    let input = ""

    function send() {
        test_messages = [...test_messages, input]
        input = ""
    }
</script>
<div class="flex h-full flex-col">
    <h1>Channel: {data.channel_uid}</h1>
    <div class="flex-auto overflow-x-auto">
        {#each test_messages as message}
            <div class="chat chat-end">
                <div class="chat-bubble">
                    {message}
                </div>
            </div>
        {/each}
    </div>
    <form class="flex-none input-group" on:submit={send}>
        <input bind:value={input} autofocus autocomplete="" class="flex-auto input" type="text" placeholder="Message">
        <button class="btn">Send</button>
    </form>
</div>
