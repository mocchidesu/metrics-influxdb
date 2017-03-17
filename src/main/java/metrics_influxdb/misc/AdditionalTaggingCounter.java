package metrics_influxdb.misc;

import com.codahale.metrics.Counter;

import java.util.Map;

/**
 * Just mark an implementation of Counter can have additional tags.
 * Created by Hidetomo on 3/16/17.
 */
public interface AdditionalTaggingCounter {
    String SEPARATOR = ":::";

    Map<String, Counter> getIndividualCountPerTag();

}
