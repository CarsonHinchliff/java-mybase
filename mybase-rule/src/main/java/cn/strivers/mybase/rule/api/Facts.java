package cn.strivers.mybase.rule.api;

import java.util.*;

import static java.lang.String.format;

/**
 * 评估和执行因子
 */
public class Facts implements Iterable<Map.Entry<String, Object>> {

    private Map<String, Object> facts = new HashMap<>();

    public Object put(String name, Object fact) {
        Objects.requireNonNull(name);
        return facts.put(name, fact);
    }

    public Object remove(String name) {
        Objects.requireNonNull(name);
        return facts.remove(name);
    }

    public <T> T get(String name) {
        Objects.requireNonNull(name);
        return (T) facts.get(name);
    }

    public Map<String, Object> asMap() {
        return facts;
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return facts.entrySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        List<Map.Entry<String, Object>> entries = new ArrayList<>(facts.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Object> entry = entries.get(i);
            stringBuilder.append(format(" { %s : %s } ", entry.getKey(), String.valueOf(entry.getValue())));
            if (i < entries.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
