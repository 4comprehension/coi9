package com.pivovarit.summaries.domain;

record OutboxRecord(long id, String type, String payload) {
}
