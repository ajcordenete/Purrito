package com.aljon.purrito.data

import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.remote.FeedDTO


/**
 * Converts network object Feed to domain object being used by the UI
 */
fun List<FeedDTO>.remoteToDomainModel() : List<Feed> {
    return map {
        Feed(it.id, it.url)
    }
}