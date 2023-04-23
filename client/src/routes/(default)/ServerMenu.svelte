<script lang="ts">
    import Icon from '@iconify/svelte';
    import homeRounded from '@iconify/icons-material-symbols/home-rounded';

    let fill: string[] = Array(1000).fill("a");
</script>

<div class="flex-none bg-base-200 flex flex-col">
    <a class="btn btn-square mx-1 text-2xl" href="/">
        <Icon icon={homeRounded} inline={true}/>
    </a>
    <div class="divider my-0.5"></div>
    <div class="overflow-x-auto flex-auto">
        <ul>
            {#each fill as item}
                <li>
                    <button class="
                            btn rounded-[100%] btn-circle
                            hover:rounded-2xl transition-all duration-300 my-0.5 mx-1 ease-in-out">
                        {item}
                    </button>
                </li>
            {/each}
        </ul>
    </div>

</div>