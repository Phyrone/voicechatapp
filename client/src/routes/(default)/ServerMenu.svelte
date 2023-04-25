<script lang="ts">
    import Icon from '@iconify/svelte';
    import homeRounded from '@iconify/icons-material-symbols/home-rounded';
    import {afterNavigate} from "$app/navigation";

    let fill: number[] = Array(100);

    //fill array with random numbers
    for (let i = 0; i < fill.length; i++) {
        fill[i] = i;
    }

    export type SelectedServer = { uid: string } | "home" | undefined
    let selected_server: SelectedServer = undefined

    afterNavigate((navEvent) => {
        let serverUID = navEvent.to?.params?.server_uid
        if (serverUID)
            selected_server = {
                uid: serverUID,
            }
        else
            selected_server = "home"
    })

    //$: console.log(selected_server)

</script>
<style lang="sass">

    .btn-active
      @apply text-error

</style>

<div class="flex-none bg-base-300 flex flex-col">
    <a class="btn btn-square mx-1 text-2xl" href="/" class:btn-active={selected_server==="home"}>
        <Icon icon={homeRounded} inline={true}/>
    </a>
    <div class="divider my-0.5"></div>
    <div class="overflow-x-auto flex-auto">
        <ul>
            {#each fill as item}
                <li>
                    <a href="/server/{item}" class:btn-active={selected_server?.uid===item.toString()} class="
                            btn rounded-[100%] btn-circle
                            hover:rounded-2xl transition-all duration-300 my-0.5 mx-1 ease-in-out">
                        {item}
                    </a>
                </li>
            {/each}
        </ul>
    </div>

</div>