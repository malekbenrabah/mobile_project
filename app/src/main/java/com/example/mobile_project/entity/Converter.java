package com.example.mobile_project.entity;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    @TypeConverter
    public static List<Integer> fromString(String value) {
        List<Integer> postsList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            String[] postsArray = value.split(",");
            for (String postId : postsArray) {
                try {
                    postsList.add(Integer.parseInt(postId));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return postsList;
    }

    @TypeConverter
    public static String fromList(List<Integer> postsList) {
        StringBuilder value = new StringBuilder();
        for (Integer postId : postsList) {
            value.append(postId).append(",");
        }
        return value.toString();
    }
}
