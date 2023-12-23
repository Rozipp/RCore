package ua.rozipp.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ua.rozipp.core.RCore;

import java.util.function.Consumer;

public class LinearMovement {

    private long begin;       // начало анимации для подсчета конца полета
    private final Location location; // последняя посчитанная локация
    private long time;    // время во время последнего пересчета локации
    private final int lifeTime;     // время полета до конечной точки
    private final Vector direction; // направление и скорость за милисекунду
    private final Consumer<Location> consumer;

    /**
     * Рисует в воздухе линию.
     *
     * @param begin    - начало анимации
     * @param end      - конец анимации
     * @param lifeTime - время за которое анимация долетит до конца
     */

    public LinearMovement(Location begin, Location end, int lifeTime, Consumer<Location> consumer) {
        this(begin, end.subtract(begin).toVector().multiply(0.001 / lifeTime),
                lifeTime, consumer);
    }

    public LinearMovement(Location begin, Vector direction, int lifeTime, Consumer<Location> consumer) {
        this.location = begin;
        this.direction = direction;
        this.lifeTime = lifeTime * 1000; // интервал переводим в милисекунды
        this.consumer = consumer;
    }

    public Location nextLocation() {
        long dt = (time - begin);  // время в милисекундах которое прошло с прошлого подсчета
        return location.clone().add(direction.getX() * dt, direction.getY() * dt, direction.getZ() * dt); //умножаем вектор скорости на dt
    }

    public void beginDrawNow() {
        this.begin = TimeUtils.now();
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                time = TimeUtils.now();
                if (time - begin > lifeTime) this.cancel();
                consumer.accept(nextLocation());
            }
        };
        task.runTaskTimer(RCore.getInstance(), 1, 1);
    }

}
