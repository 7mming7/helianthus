package com.ha.hjob.hjobType;

import com.ha.hjob.Hjob;
import com.ha.utils.Props;

import java.util.HashMap;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/7/5 上午10:13
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HjobTypePluginSet {

    private Map<String, Class<? extends Hjob>> hjobToClass;
    private Map<String, Props> pluginJobPropsMap;
    private Map<String, Props> pluginLoadPropsMap;

    private Props commonJobProps;
    private Props commonLoadProps;

    /**
     * Base constructor
     */
    public HjobTypePluginSet() {
        hjobToClass = new HashMap<String, Class<? extends Hjob>>();
        pluginJobPropsMap = new HashMap<String, Props>();
        pluginLoadPropsMap = new HashMap<String, Props>();
    }

    /**
     * Copy constructor
     *
     * @param clone
     */
    public HjobTypePluginSet(HjobTypePluginSet clone) {
        hjobToClass = new HashMap<String, Class<? extends Hjob>>(clone.hjobToClass);
        pluginJobPropsMap = new HashMap<String, Props>(clone.pluginJobPropsMap);
        pluginLoadPropsMap = new HashMap<String, Props>(clone.pluginLoadPropsMap);
        commonJobProps = clone.commonJobProps;
        commonLoadProps = clone.commonLoadProps;
    }

    /**
     * Sets the common properties shared in every jobtype
     *
     * @param commonJobProps
     */
    public void setCommonPluginJobProps(Props commonJobProps) {
        this.commonJobProps = commonJobProps;
    }

    /**
     * Sets the common properties used to load every plugin
     *
     * @param commonLoadProps
     */
    public void setCommonPluginLoadProps(Props commonLoadProps) {
        this.commonLoadProps = commonLoadProps;
    }

    /**
     * Gets common properties for every jobtype
     *
     * @return
     */
    public Props getCommonPluginJobProps() {
        return commonJobProps;
    }

    /**
     * Gets the common properties used to load a plugin
     *
     * @return
     */
    public Props getCommonPluginLoadProps() {
        return commonLoadProps;
    }

    /**
     * Get the properties for a jobtype used to setup and load a plugin
     *
     * @param jobTypeName
     * @return
     */
    public Props getPluginLoaderProps(String jobTypeName) {
        return pluginLoadPropsMap.get(jobTypeName);
    }

    /**
     * Get the properties that will be given to the plugin as default job
     * properties.
     *
     * @param jobTypeName
     * @return
     */
    public Props getPluginJobProps(String jobTypeName) {
        return pluginJobPropsMap.get(jobTypeName);
    }

    /**
     * Gets the plugin job runner class
     *
     * @param jobTypeName
     * @return
     */
    public Class<? extends Hjob> getPluginClass(String jobTypeName) {
        return hjobToClass.get(jobTypeName);
    }

    /**
     * Adds plugin jobtype class
     */
    public void addPluginClass(String jobTypeName,
                               Class<? extends Hjob> jobTypeClass) {
        hjobToClass.put(jobTypeName, jobTypeClass);
    }

    /**
     * Adds plugin job properties used as default runtime properties
     */
    public void addPluginJobProps(String jobTypeName, Props props) {
        pluginJobPropsMap.put(jobTypeName, props);
    }

    /**
     * Adds plugin load properties used to load the plugin
     */
    public void addPluginLoadProps(String jobTypeName, Props props) {
        pluginLoadPropsMap.put(jobTypeName, props);
    }
}
