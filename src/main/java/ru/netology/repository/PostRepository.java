package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

// Stub
public class PostRepository {

    private final Map<Long, Post> map = new ConcurrentHashMap<>();
    private final AtomicReference<Long> counter = new AtomicReference<>(0L);
    //private List<Post> list = new ArrayList<Post>(map.values());

    AtomicReference<Optional<Post>> optional = new AtomicReference<>();

    public List<Post> all() {
        List<Post> list = new ArrayList<Post>(map.values());
        System.out.println("list: " + list.toString());
        //return Collections.emptyList();
        return list;
    }

    public Optional<Post> getById(long id) {
        if (!map.containsKey(id)) {
            return Optional.empty();
        }
        optional = new AtomicReference<>(Optional.of(map.get(id)));
        return optional.get();
    }

    public Post save(Post post) {
        if (post.getId() != 0) {
            if (map.containsKey(post.getId())) {
                map.put(post.getId(), post);
                return post;
            } else {
                post.setContent("Post not found");
                return post;
            }
        } else {
            long current = counter.get();
            System.out.println(current);
            long next = current + 1;
            System.out.println(next);
            counter.set(next);
            post.setId(next);
            map.put(next, post);
            System.out.println("Post saved");
            System.out.println(map.toString());
            return post;
        }
    }

    public boolean removeById(long id) {
        if (map.containsKey(id)) {
            map.remove(id);
            System.out.println("removed post " + id);
            return true;
        } else {
            return false;
        }
    }
}
