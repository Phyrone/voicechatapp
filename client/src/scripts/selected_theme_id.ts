import {localStorageStore} from '@skeletonlabs/skeleton';

export type Theme = {
    name: string,
    id: string,
};
export const ThemeSkeleton: Theme = {
    name: "Skeleton",
    id: "skeleton",
}
export const ThemeModern: Theme = {
    name: "Modern",
    id: "modern",
}
export const ThemeRocket: Theme = {
    name: "Rocket",
    id: "rocket",
}
export const ThemeSeafoam: Theme = {
    name: "Seafoam",
    id: "seafoam",
}
export const ThemeVintage: Theme = {
    name: "Vintage",
    id: "vintage",
}
export const ThemeSahara: Theme = {
    name: "Sahara",
    id: "sahara",
}
export const ThemeHamlindigo : Theme = {
    name: "Hamlindigo",
    id: "hamlindigo",
}
export const ThemeGoldNouveau : Theme = {
    name: "Gold Nouveau",
    id: "gold-nouveau",
}
export const ThemeCrimson : Theme = {
    name: "Crimson",
    id: "crimson",
}

export const themes: Theme[] = [
    ThemeSkeleton,
    ThemeModern,
    ThemeRocket,
    ThemeSeafoam,
    ThemeVintage,
    ThemeSahara,
    ThemeHamlindigo,
    ThemeGoldNouveau,
    ThemeCrimson,
];


export const selected_theme_id = localStorageStore<string>('selected-theme', ThemeSkeleton.id,{
    storage: 'local',
})