export function convertTimestamp(timestamp) {
    const time = new Date(timestamp * 1000);
    return time;
}

export function getDuration(time1, time2) {
    const duration = time1 - time2;
    return duration;
}