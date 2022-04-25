
export function getDuration(time1, time2) {
    const difference = time1 - time2;
    const duration = Math.floor(difference / 1000/60/60/24);
    return duration;
}