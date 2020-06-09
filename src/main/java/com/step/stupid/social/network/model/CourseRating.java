package com.step.stupid.social.network.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
//
//@Entity
//@Table
//@Getter
//@Setter
public class CourseRating {

//    @EmbeddedId
    private CompositeUserCourseKey embeddedId;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("course_id")
    @JoinColumn(name = "course_id")
    private Course course;

    private double rating;
}
