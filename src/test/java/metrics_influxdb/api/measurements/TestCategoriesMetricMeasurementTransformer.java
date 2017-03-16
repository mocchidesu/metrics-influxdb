package metrics_influxdb.api.measurements;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.collection.IsMapContaining.hasValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
//import java.util.function.Consumer;

import org.junit.Test;

public class TestCategoriesMetricMeasurementTransformer {
    @Test
    public void withoutCategoriesActsAsNOOP() {
        final CategoriesMetricMeasurementTransformer noCategories = new CategoriesMetricMeasurementTransformer();
        
        List<String> metricsName = Arrays.asList("metric", "a.metric", "a.metric.that.should.define.different.name.spaces");
        
//        metricsName.forEach(new Consumer<String>() {
//                                @Override
//                                public void accept(String s) {
//                                    assertThat(noCategories.measurementName(s), is(s));
//                                    assertThat(noCategories.tags(s), notNullValue());
//                                    assertThat(noCategories.tags(s).entrySet(), empty());
//                                }
//                            }
//
//                n -> {
//                    assertThat(noCategories.measurementName(n), is(n));
//                    assertThat(noCategories.tags(n), notNullValue());
//                    assertThat(noCategories.tags(n).entrySet(), empty());
//                );
    }
    
    @Test
    public void fromCategories() {
        final String categoryServer = "server";
        final String categoryType = "type";
        CategoriesMetricMeasurementTransformer serverAndType = new CategoriesMetricMeasurementTransformer(categoryServer, categoryType);
        String metricName = "actarus.prod.cpu_load";
        
        assertThat(serverAndType.measurementName(metricName), is("cpu_load"));
        Map<String, String> tags = serverAndType.tags(metricName);
        assertThat(tags, notNullValue());
        assertThat(tags, hasEntry(categoryServer, "actarus"));
        assertThat(tags, hasEntry(categoryType, "prod"));
    }
    
    @Test
    public void measurementNameUsesRemainingSubStringsIfGreaterThanCategoriesLength() {
        final String categoryServer = "server";
        final String categoryType = "type";
        CategoriesMetricMeasurementTransformer serverAndType = new CategoriesMetricMeasurementTransformer(categoryServer, categoryType);
        String metricName = "actarus.prod.core_4.cpu_load";
        
        assertThat(serverAndType.measurementName(metricName), is("core_4.cpu_load"));
        Map<String, String> tags = serverAndType.tags(metricName);
        assertThat(tags, notNullValue());
        assertThat(tags, hasEntry(categoryServer, "actarus"));
        assertThat(tags, hasEntry(categoryType, "prod"));
        assertThat(tags, not(hasValue("core_4")));
    }
    
    @Test
    public void measurementNameUsesLastSubStringsEvenIfCategoriesLengthIsGreater() {
        final String categoryServer = "server";
        final String categoryType = "type";
        final String categoryCores = "cores";
        
        CategoriesMetricMeasurementTransformer serverAndType = new CategoriesMetricMeasurementTransformer(categoryServer, categoryType, categoryCores);
        String metricName = "actarus.prod.cpu_load";
        
        assertThat(serverAndType.measurementName(metricName), is("cpu_load"));
        Map<String, String> tags = serverAndType.tags(metricName);
        assertThat(tags, notNullValue());
        assertThat(tags, hasEntry(categoryServer, "actarus"));
        assertThat(tags, hasEntry(categoryType, "prod"));
        assertThat(tags, not(hasKey(categoryCores)));
    }
}
