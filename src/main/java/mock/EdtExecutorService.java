package mock;

public class EdtExecutorService {
    public static class C {
        static C i = new C();
        public Thread schedule(MRunnable r, long ms, Object o) {
            var t = new Thread(() -> {
                try {
                    Thread.sleep(ms);
                    r.run();
                } catch (InterruptedException e) {}
            });
            t.start();
            return t;
        }
    }
    public static C getScheduledExecutorInstance() {
        return C.i;
    }
}
