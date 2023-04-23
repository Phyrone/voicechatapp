import {writable} from "svelte/store";
import {persisted} from "svelte-local-storage-store";
import availableThemes from "./themes.json"

export type Theme = {
    displayname: string,
    id: string,
};
//
export const ThemeDefault: Theme = {
    displayname: 'Default',
    id: 'default',
}

export const themes: Theme[] = availableThemes


export const theme_settings = persisted<string>('selected-theme', ThemeDefault.id, {
    storage: 'local',
})

export const theme_settings_dark_mode = persisted<boolean | undefined>('selected-theme-dark-mode', undefined, {
    storage: 'session',
})
export const system_dark_mode = writable<boolean>(false)
export const is_dark_mode = writable<boolean>(false)

