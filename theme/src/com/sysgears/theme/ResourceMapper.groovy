package com.sysgears.theme

import com.sysgears.grain.taglib.Site
import org.apache.commons.lang.StringUtils

import java.util.concurrent.TimeUnit

/**
 * Change pages urls and extend models.
 */
class ResourceMapper {

    /**
     * Site reference, provides access to site configuration.
     */
    private final Site site

    public ResourceMapper(Site site) {
        this.site = site
    }

    /**
     * This closure is used to transform page URLs and page data models.
     */
    def map = { resources ->

        def refinedResources = resources.findResults(filterPublished).collect { Map resource ->
            fillDates << addDescription << resource
        }

        int week = 0
        def prevWeek = null
        refinedResources.findAll {
            it.layout == 'weekly'
        }.sort {
            it.date
        }.each {
            it.put('number', week)
            if (prevWeek != null) {
                it.put('prevWeekUrl', prevWeek.url)
                prevWeek.put('nextWeekUrl', it.url)
            }
            prevWeek = it
            week++
        }

        refinedResources = refinedResources.collect {
            fillWeekly << it
        }

        refinedResources
    }

    /**
     * Excludes resources with published property set to false,
     * unless it is allowed to show unpublished resources in SiteConfig.
     */
    private def filterPublished = { Map it ->
        (it.published != false || site.show_unpublished) ? it : null
    }

    /**
     * Fills in page `date` and `updated` fields 
     */
    private def fillDates = { Map it ->
        def update = [date: it.date ? Date.parse(site.datetime_format, it.date) : new Date(it.dateCreated as Long),
                updated: it.updated ? Date.parse(site.datetime_format, it.updated) : new Date(it.lastUpdated as Long)]
        if (it.layout == 'weekly') {
            def m = it.location =~ /\/(\d{4}-\d\d-\d\d)\.adoc/
            if (m.find()) {
                def publishWeek = Date.parse('yyyy-MM-dd', m.group(1))
                update.week = new Date(publishWeek.time - TimeUnit.DAYS.toMillis(7))
                update.weekEnd = publishWeek
                update.date = update.weekEnd
            }
        }
        it + update
    }

    private def fillWeekly = { Map it ->
        def update = [:]
        if (it.layout == 'weekly') {
            update.title = "Development Update, week #${it.number}"
        }
        return it + update
    }

    private def addDescription = { Map it ->
        String html = it.render().content ?: ''
        if (html.contains('<!--more-->')) {
            html = html.split('<!--more-->').head()
        }
        html = html
                .replaceAll(~/\n/, ' ')
                .replaceAll(~/<(br|p|li).*?>/, '\n')
                .replaceAll(~/<.+?>/, '')
                .trim()
        def update = [
                brief: StringUtils.abbreviate(html, 400).replaceAll(~/\n/, '<br/>'),
                description: StringUtils.abbreviate(html, 160).replaceAll(~/[\n"']/, '').replaceAll(~/\s+/, ' ')
        ]
        it + update
    }
}
