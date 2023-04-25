<script lang="ts">
    import DefaultAppShell from "./(default)/DefaultAppShell.svelte";
    import { page } from '$app/stores';

</script>

<DefaultAppShell>
    <h1>{$page.status}: {$page.error.message}</h1>
</DefaultAppShell>