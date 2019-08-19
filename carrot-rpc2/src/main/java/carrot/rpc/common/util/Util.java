package carrot.rpc.common.util;

public class Util {
    private static long requestId = 0;

    public synchronized static long generateRequestId() {
        return requestId++;
    }
}
