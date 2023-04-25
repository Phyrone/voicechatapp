<script lang="ts">

    import ServerMenu from "./ServerMenu.svelte";
</script>
<div class="flex h-full">
    <ServerMenu/>
    <div class="flex-auto overflow-x-auto"><slot/></div>
</div>
