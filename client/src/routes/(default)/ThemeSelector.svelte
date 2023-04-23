<script lang="ts">
    import type {Theme} from "../../scripts/theme_settings";
    import {theme_settings, themes} from "../../scripts/theme_settings";

</script>

<select class:select={true} bind:value={$theme_settings}>
    {#each themes as theme}
        <option value={theme.id}>{theme.displayname}</option>
    {/each}
</select>