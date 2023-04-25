<script lang="ts">

    let num = 0;

    let array: number[] = [];
    $: {
        array = new Array(num).fill(0);
    }
</script>

<h1>Welcome to SvelteKit</h1>
<p>Visit <a href="https://kit.svelte.dev">kit.svelte.dev</a> to read the documentation</p>
<button class="btn variant-filled" on:click={()=>num++}>{num}</button>

{#each array as _, i}
    <div class="card">
        <h2>Card {i}</h2>
        <p>
            Adipisicing excepteur excepteur laborum nisi aute incididunt excepteur magna ullamco ullamco enim. Magna pariatur
            deserunt minim cillum. Duis duis qui ex deserunt ipsum fugiat incididunt reprehenderit duis sint eu consequat.
            Voluptate deserunt ut ullamco consectetur officia Lorem elit elit. Labore deserunt ullamco veniam proident est velit
            incididunt ut. Duis et cupidatat sunt duis incididunt nisi consequat id esse amet aliqua. Sit cupidatat irure ea do
            consectetur incididunt aliqua nulla elit.
        </p>
    </div>
{/each}
<div id="end"></div>
