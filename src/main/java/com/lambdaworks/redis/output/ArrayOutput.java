package com.lambdaworks.redis.output;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.protocol.CommandOutput;

/**
 * {@link java.util.List} of objects and lists to support dynamic nested structures (List with mixed content of values and
 * sublists).
 * 
 * @param <K> Key type.
 * @param <V> Value type.
 * 
 * @author Mark Paluch
 */
public class ArrayOutput<K, V> extends CommandOutput<K, V, List<Object>> {
    private List<Object> nested;
    private Stack<Integer> counts = new Stack<Integer>();
    private Stack<List<Object>> stack = new Stack<List<Object>>();

    public ArrayOutput(RedisCodec<K, V> codec) {
        super(codec, new ArrayList<Object>());
    }

    @Override
    public void set(ByteBuffer bytes) {
        if (bytes != null) {
            V value = codec.decodeValue(bytes);
            stack.peek().add(value);
        }
    }

    @Override
    public void set(long integer) {
        stack.peek().add(integer);
    }

    @Override
    public void complete(int depth) {
        if (counts.isEmpty()) {
            return;
        }

        if (depth == stack.size()) {
            if (stack.peek().size() == counts.peek()) {
                List<Object> pop = stack.pop();
                counts.pop();
                if (!stack.isEmpty()) {
                    stack.peek().add(pop);
                }
            }
        }
    }

    @Override
    public void multi(int count) {
        if (stack.isEmpty()) {
            stack.push(output);
        } else {
            stack.push(new ArrayList<Object>(count));

        }
        counts.push(count);
    }
}
