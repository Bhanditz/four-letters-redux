package nz.bradcampbell.fourletters;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RxJavaResetRule implements TestRule {

    @Override public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                reset();
                RxAndroidPlugins.getInstance().reset();

                RxJavaPlugins.getInstance().registerSchedulersHook(new SchedulerHook());
                RxAndroidPlugins.getInstance().registerSchedulersHook(new AndroidSchedulerHook());

                base.evaluate();

                reset();
                RxAndroidPlugins.getInstance().reset();
            }
        };
    }

    private void reset() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = RxJavaPlugins.class.getDeclaredMethod("reset");
        m.setAccessible(true);
        m.invoke(RxJavaPlugins.getInstance());
    }

    private static class SchedulerHook extends RxJavaSchedulersHook {
        @Override
        public Scheduler getIOScheduler() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler getNewThreadScheduler() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler getComputationScheduler() {
            return Schedulers.immediate();
        }
    }

    private static class AndroidSchedulerHook extends RxAndroidSchedulersHook {
        @Override public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    }
}
