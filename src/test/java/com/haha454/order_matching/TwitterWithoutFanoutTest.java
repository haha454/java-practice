package com.haha454.order_matching;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwitterWithoutFanoutTest {

    @Test
    void verify_postTweet() {
        var twitter = new TwitterWithoutFanout();
        twitter.postTweet(1, 5);
        assertIterableEquals(List.of(5), twitter.getNewsFeed(1));
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        assertIterableEquals(List.of(6, 5), twitter.getNewsFeed(1));
        twitter.unfollow(1, 2);
        assertIterableEquals(List.of(5), twitter.getNewsFeed(1));
    }

    @Test
    void verify_postTweet_with_nonchronological_tweet_id() {
        var twitter = new TwitterWithoutFanout();
        twitter.postTweet(1, 5);
        twitter.postTweet(1, 3);
        assertIterableEquals(List.of(3, 5), twitter.getNewsFeed(1));
    }

    @Test
    void verify_postTweet_with_nonchronological_tweet_id_2() {
        var twitter = new TwitterWithoutFanout();
        twitter.postTweet(1, 5);
        twitter.postTweet(1, 3);
        twitter.postTweet(1, 101);
        twitter.postTweet(1, 13);
        twitter.postTweet(1, 10);
        twitter.postTweet(1, 2);
        twitter.postTweet(1, 94);
        twitter.postTweet(1, 505);
        twitter.postTweet(1, 333);
        assertIterableEquals(List.of(333,505,94,2,10,13,101,3, 5), twitter.getNewsFeed(1));
    }

    @Test
    void verify_postTweet_with_nonchronological_tweet_id_more_than_10_tweets() {
        var twitter = new TwitterWithoutFanout();
        twitter.postTweet(1, 5);
        twitter.postTweet(1, 3);
        twitter.postTweet(1, 101);
        twitter.postTweet(1, 13);
        twitter.postTweet(1, 10);
        twitter.postTweet(1, 2);
        twitter.postTweet(1, 94);
        twitter.postTweet(1, 505);
        twitter.postTweet(1, 333);
        twitter.postTweet(1, 1001);
        twitter.postTweet(1, 1002);
        assertIterableEquals(List.of(1002, 1001, 333,505,94,2,10,13,101,3), twitter.getNewsFeed(1));
    }
}