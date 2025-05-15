package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedRepository extends JpaRepository<Feed, UUID> {

}
