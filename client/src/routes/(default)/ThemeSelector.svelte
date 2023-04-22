<script lang="ts">
    import type {Theme} from "../../scripts/selected_theme_id";
    import {selected_theme_id, themes} from "../../scripts/selected_theme_id";

</script>

<select class:select={true} bind:value={$selected_theme_id}>
    {#each themes as theme}
        <option value={theme.id}>{theme.name}</option>
    {/each}
</select>