package cn.cheney.uploader.util


class Logger {

    private static final String TAG = "Maven-Uploader"

    private Logger() {
    }

    static void d(String msg) {
        System.out.println("D/" + TAG + ": " + msg)
    }

    static void d(Object msg) {
        System.out.println("D/" + TAG + ": " + String.valueOf(msg))
    }

    static void e(String msg) {
        System.err.println("E/" + TAG + ": " + msg)
    }

    static void e(Object msg) {
        System.err.println("E/" + TAG + ": " + String.valueOf(msg))
    }

}