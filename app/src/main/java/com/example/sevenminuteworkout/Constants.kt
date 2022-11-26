package com.example.sevenminuteworkout

object Constants {
    fun defaultExerciseList(): ArrayList<Exercise> {
        return arrayListOf<Exercise>(
            Exercise(0, "Jumping Jacks", R.drawable.ic_01_jumping_jacks),
            Exercise(1, "Wall Squats", R.drawable.ic_02_wall_sit_lower_body),
            Exercise(2, "Push Ups", R.drawable.ic_03_pushup_upper_body),
            Exercise(3, "Crunches", R.drawable.ic_04_abdominal_crunch_core),
            Exercise(4, "Chair Steps", R.drawable.ic_05_stepup_onto_chair),
            Exercise(5, "Squats", R.drawable.ic_06_squat_lower_body),
            Exercise(6, "Triceps Dips", R.drawable.ic_07_triceps_dip),
            Exercise(7, "Plank", R.drawable.ic_08_plank_core),
            Exercise(8, "High Knees", R.drawable.ic_09_high_knees_lower_body),
            Exercise(9, "Lunges", R.drawable.ic_10_lunge_lower_body),
            Exercise(10, "Push Ups With Rotation", R.drawable.ic_11_pushup_and_rotation),
            Exercise(11, "Side Planks", R.drawable.ic_12_side_plank_core)
        )
    }
}