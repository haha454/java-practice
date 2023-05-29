package com.haha454.order_matching;

public class TwitterWithFanout {
//    private final Map<Integer, NavigableSet<Integer>> feedsByUser; // userId -> postIds of followees and self
//    private final Map<Integer, List<Integer>> postsByUser; // userId -> postIds of self
//    private final Map<Integer, Set<Integer>> followersByUser; // userId -> followers
//
//    public Twitter() {
//        feedsByUser = new HashMap<>();
//        postsByUser = new HashMap<>();
//        followersByUser = new HashMap<>();
//    }
//
//    public void postTweet(int userId, int tweetId) {
//        if (postsByUser.containsKey(userId)) {
//            postsByUser.get(userId).add(tweetId);
//        } else {
//            postsByUser.put(userId, new TreeSet<>(List.of(tweetId)));
//        }
//        if (followersByUser.containsKey(userId)) {
//            for (int follower : followersByUsers.get(userId)) {
//                assert feedsByUser.containsKey(follower);
//                feedsByUser.get(follower).add(tweetId);
//            }
//        }
//    }
//
//    public List<Integer> getNewsFeed(int userId) {
//        if (!feedsByUsers.containsKey(userId)) {
//            return List.of();
//        }
//        return feedsByUsers.get(userId).descendingSet().stream().limit(10).collect(Collectors.toList());
//    }
//
//    public void follow(int followerId, int followeeId) {
//        if (follwersByUser.containsKey(followeeId) && follwersByUser.get(followeeId).contains(followerId)) {
//            return;
//        }
//
//        if (!followersByUser.containsKey(followeeId)) {
//            followersByUser.put(followeeId, new HashSet<>(List.of(followerId)));
//        } else {
//            followersByUser.get(followeeId).add(followerId);
//        }
//
//
//    }
//
//    public void unfollow(int followerId, int followeeId) {
//
//    }
}
