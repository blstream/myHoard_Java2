package com.blstream.myhoard.aspect;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

@Aspect
@Order(3)
public class CountingAspect {

    private static class Int {
        private int value = 1;

        public void inc() {
            value++;
        }

        public int getInt() {
            return value;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof Int && value == ((Int)o).value;
        }
    }

    private final long FREQUENCY = 30 * 1000;    // co 30 sekund
    private final Map<String, Int> stats = new HashMap<>();
    private Date latest = new Date();

    @Value("${aspect.methodInvocationCounter:true}")
    private boolean enabled;

    @Pointcut("within(com.blstream.myhoard..*)")
    public void func() {}

    @Before("func()")
    public void count(JoinPoint point) {
        if (enabled) {
            String methodName = point.getSignature().toShortString();
            if (stats.containsKey(methodName))
                stats.get(methodName).inc();
            else
                stats.put(methodName, new Int());

            Date current = new Date();
            if (current.getTime() - latest.getTime() > FREQUENCY) {
                StringBuilder builder = new StringBuilder("Statystyki:\n");
                for (Entry<String, Int> e : stats.entrySet())
                    builder.append(String.format("\t%s: %d\n", e.getKey(), e.getValue().getInt()));
                Logger.getRootLogger().info(builder.toString());
            }
            latest = current;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
