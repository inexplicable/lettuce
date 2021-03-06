package com.lambdaworks.redis;

import java.util.List;

/**
 * Synchronous executed commands for Scripting.
 * 
 * @param <K> Key type.
 * @param <V> Value type.
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 3.0
 */
public interface RedisScriptingConnection<K, V> {
    /**
     * Execute a Lua script server side.
     * 
     * @param script Lua 5.1 script.
     * @param type output type
     * @param keys key names
     * @return script result
     */
    <T> T eval(String script, ScriptOutputType type, K... keys);

    /**
     * Execute a Lua script server side.
     * 
     * @param script Lua 5.1 script.
     * @param type the type
     * @param keys the keys
     * @param values the values
     * @return script result
     */
    <T> T eval(String script, ScriptOutputType type, K[] keys, V... values);

    /**
     * Evaluates a script cached on the server side by its SHA1 digest
     * 
     * @param digest SHA1 of the script
     * @param type the type
     * @param keys the keys
     * @return script result
     */
    <T> T evalsha(String digest, ScriptOutputType type, K... keys);

    /**
     * Execute a Lua script server side.
     * 
     * @param digest SHA1 of the script
     * @param type the type
     * @param keys the keys
     * @param values the values
     * @return script result
     */
    <T> T evalsha(String digest, ScriptOutputType type, K[] keys, V... values);

    /**
     * Check existence of scripts in the script cache.
     * 
     * @param digests
     * @return List&lt;Boolean&gt; array-reply The command returns an array of integers that correspond to the specified SHA1
     *         digest arguments. For every corresponding SHA1 digest of a script that actually exists in the script cache, an 1
     *         is returned, otherwise 0 is returned.
     */
    List<Boolean> scriptExists(String... digests);

    /**
     * Remove all the scripts from the script cache.
     * 
     * @return String simple-string-reply
     */
    String scriptFlush();

    /**
     * Kill the script currently in execution.
     * 
     * @return String simple-string-reply
     */
    String scriptKill();

    /**
     * Load the specified Lua script into the script cache.
     * 
     * @param script
     * @return String bulk-string-reply This command returns the SHA1 digest of the script added into the script cache.
     */
    String scriptLoad(V script);
}
