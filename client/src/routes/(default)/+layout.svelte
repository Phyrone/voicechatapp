<script lang="ts">


    import ThemeSelector from "./ThemeSelector.svelte";
    import ServerMenu from "./ServerMenu.svelte";
</script>
<div class="flex h-full">
    <ServerMenu/>
    <div class="flex-auto overflow-x-auto"><slot/></div>
</div>
