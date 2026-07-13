package com.example.dtos.profile;

public class StudentRankingDto {

    private int position;
    private int totalStudents;

    public StudentRankingDto() {
    }

    public StudentRankingDto(
            int position,
            int totalStudents
    ) {
        this.position = position;
        this.totalStudents = totalStudents;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }
}