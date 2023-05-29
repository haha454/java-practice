package com.haha454.order_matching;

import java.util.*;

class TwitterWithoutFanout {
    // follower ->(follow) followee
    private final Map<Integer, List<Integer>> postsByUser; // userId -> postIds of self
    private final Map<Integer, Set<Integer>> followeesByUser; // userId -> followees
    private final Map<Integer, Integer> tweetsTimestamp; // tweetId -> timestamp;

    public TwitterWithoutFanout() {
        postsByUser = new HashMap<>();
        followeesByUser = new HashMap<>();
        tweetsTimestamp = new HashMap<>();
    }

    public void postTweet(int userId, int tweetId) {
        assert !tweetsTimestamp.containsKey(tweetId);
        if (postsByUser.containsKey(userId)) {
            postsByUser.get(userId).add(tweetId);
        } else {
            postsByUser.put(userId, new ArrayList<>(List.of(tweetId)));
        }
        tweetsTimestamp.put(tweetId, tweetsTimestamp.size());
    }

    public List<Integer> getNewsFeed(int userId) {
        var pq = new PriorityQueue<Integer>((e, v) -> tweetsTimestamp.get(e) - tweetsTimestamp.get(v));
        if (postsByUser.containsKey(userId)) {
            postsByUser.get(userId).forEach(tweetId -> this.addToPq(pq, tweetId));
        }
        if (followeesByUser.containsKey(userId)) {
            followeesByUser.get(userId).forEach(followee -> {
                if (postsByUser.containsKey(followee)) {
                    postsByUser.get(followee).forEach(tweetId -> this.addToPq(pq, tweetId));
                }
            });
        }

        var result = new ArrayList<Integer>(pq.size());
        while (!pq.isEmpty()) {
            result.add(pq.remove());
        }

        Collections.reverse(result);
        return result;
    }

    private void addToPq(PriorityQueue<Integer> pq, Integer tweetId) {
        pq.add(tweetId);
        if (pq.size() > 10) {
            pq.remove();
        }
    }

    public void follow(int followerId, int followeeId) {
        if (followeesByUser.containsKey(followerId)) {
            followeesByUser.get(followerId).add(followeeId);
        } else {
            followeesByUser.put(followerId, new HashSet<>(Set.of(followeeId)));
        }
    }

    public void unfollow(int followerId, int followeeId) {
        if (!followeesByUser.containsKey(followerId)) {
            return;
        }
        followeesByUser.get(followerId).remove(followeeId);
    }
}
