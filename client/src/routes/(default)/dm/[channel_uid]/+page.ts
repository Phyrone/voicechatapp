
import type {PageLoad} from './$types';

export const load = (({params}) => {
    return {
        channel_uid: params.channel_uid,
    };
}) satisfies PageLoad;