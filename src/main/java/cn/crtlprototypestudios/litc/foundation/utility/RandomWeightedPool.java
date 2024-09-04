package cn.crtlprototypestudios.litc.foundation.utility;

import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class RandomWeightedPool<T> {
    protected List<T> pool;
    protected List<Float> weights;
    protected float weightCache;
    protected Random rand;

    public RandomWeightedPool() {
        pool = new ArrayList<T>();
        weights = new ArrayList<>();
        weightCache = 0f;
        rand = Random.create();
    }

    public void add(T t, float weight) {
        pool.add(t);
        weights.add(weight);
        weightCache += weight;
    }

    public T get(){
        if (pool.isEmpty()) return null;
        if (pool.size() == 1) return pool.getFirst();

        float generated = rand.nextFloat() * weightCache;
        for (int i = 0; i < weights.size(); i++) {
            generated -= weights.get(i);
            if (generated < 0) return pool.get(i);
        }

        return pool.getLast();
    }

    public T getUnweighted(){
        if (pool.isEmpty()) return null;
        if (pool.size() == 1) return pool.getFirst();
        int generated = rand.nextInt(pool.size()) - 1;

        if(generated < 0) return pool.getFirst();
        else return pool.get(generated);
    }
}
