package com.fitnessapp.data.local

import com.fitnessapp.data.local.entity.ExerciseEntity
import com.fitnessapp.domain.model.Difficulty
import com.fitnessapp.domain.model.MuscleGroup

object SeedData {

    val exerciseEntities: List<ExerciseEntity> = listOf(
        // CHEST
        ExerciseEntity(
            name = "Barbell Bench Press",
            description = "The classic compound chest exercise using a barbell on a flat bench.",
            muscleGroup = MuscleGroup.CHEST.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Lie flat on the bench|Grip the bar slightly wider than shoulder-width|Unrack the bar and lower it to mid-chest|Press the bar back up to full arm extension|Keep feet flat on the floor throughout"
        ),
        ExerciseEntity(
            name = "Incline Dumbbell Press",
            description = "An upper chest-focused press performed on an incline bench.",
            muscleGroup = MuscleGroup.CHEST.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Set bench to 30-45 degree incline|Hold dumbbells at chest level with palms facing forward|Press dumbbells up and slightly inward|Lower slowly to starting position|Maintain stable shoulder blades throughout"
        ),
        ExerciseEntity(
            name = "Cable Chest Fly",
            description = "An isolation exercise that targets the chest using cable machines.",
            muscleGroup = MuscleGroup.CHEST.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Set cables to shoulder height|Stand in the center, step forward slightly|Bring hands together in a hugging motion|Squeeze chest at the peak|Slowly return to start with a stretch"
        ),
        ExerciseEntity(
            name = "Push-Up",
            description = "The foundational bodyweight chest exercise, no equipment required.",
            muscleGroup = MuscleGroup.CHEST.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Place hands slightly wider than shoulders|Keep body in a straight plank line|Lower chest to near the floor|Push back up to full arm extension|Keep core tight throughout"
        ),
        ExerciseEntity(
            name = "Dips",
            description = "A compound bodyweight exercise targeting the lower chest and triceps.",
            muscleGroup = MuscleGroup.CHEST.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Grip parallel bars and lift yourself up|Lean slightly forward for chest emphasis|Lower your body until elbows reach 90 degrees|Push back up to full arm extension|Avoid locking elbows completely at the top"
        ),

        // BACK
        ExerciseEntity(
            name = "Pull-Up",
            description = "A compound pulling exercise that builds width in the back.",
            muscleGroup = MuscleGroup.BACK.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Hang from a bar with overhand grip, hands wider than shoulders|Pull your body up until chin clears the bar|Squeeze your lats at the top|Lower slowly to a dead hang|Keep core engaged throughout"
        ),
        ExerciseEntity(
            name = "Barbell Bent-Over Row",
            description = "A fundamental compound exercise for building back thickness.",
            muscleGroup = MuscleGroup.BACK.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Stand with feet shoulder-width apart, hinge at hips|Grip the bar at shoulder width with overhand grip|Pull bar to lower ribcage|Squeeze shoulder blades together at the top|Lower with control, keep back flat"
        ),
        ExerciseEntity(
            name = "Lat Pulldown",
            description = "A machine-based exercise targeting the latissimus dorsi muscle.",
            muscleGroup = MuscleGroup.BACK.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Sit at the machine and grip the bar wide|Pull the bar down to your upper chest|Squeeze lats at the bottom|Slowly return bar to full arm extension|Lean back slightly at about 10-15 degrees"
        ),
        ExerciseEntity(
            name = "Seated Cable Row",
            description = "An isolation exercise for mid-back thickness using a cable machine.",
            muscleGroup = MuscleGroup.BACK.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Sit upright and grasp the cable handle|Pull the handle to your lower abdomen|Squeeze shoulder blades together|Slowly extend arms back to start|Keep chest up and back straight"
        ),
        ExerciseEntity(
            name = "Deadlift",
            description = "The king of all compound lifts, targeting the entire posterior chain.",
            muscleGroup = MuscleGroup.BACK.name,
            difficulty = Difficulty.ADVANCED.name,
            instructions = "Stand with feet hip-width apart, bar over mid-foot|Hinge at hips and grip the bar just outside legs|Brace core, chest up, drive through the floor|Lock hips and knees out at the top|Lower with control by pushing hips back first"
        ),

        // SHOULDERS
        ExerciseEntity(
            name = "Overhead Barbell Press",
            description = "The primary compound exercise for building shoulder mass and strength.",
            muscleGroup = MuscleGroup.SHOULDERS.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Stand with bar at shoulder height, grip just outside shoulder width|Brace core and press the bar overhead|Lock out arms at the top without arching back|Lower bar to shoulder height with control|Keep elbows slightly forward of the bar"
        ),
        ExerciseEntity(
            name = "Lateral Raises",
            description = "An isolation exercise targeting the lateral deltoid for shoulder width.",
            muscleGroup = MuscleGroup.SHOULDERS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Stand holding dumbbells at your sides|Raise arms out to the sides to shoulder height|Slightly tilt little finger up at the top|Lower slowly with control|Avoid using momentum or swinging"
        ),
        ExerciseEntity(
            name = "Arnold Press",
            description = "A dumbbell shoulder press with a rotational movement for full deltoid development.",
            muscleGroup = MuscleGroup.SHOULDERS.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Start with dumbbells at chin height, palms facing you|Rotate palms outward as you press overhead|Fully extend arms at the top|Reverse the rotation as you lower|Keep core tight and avoid arching"
        ),
        ExerciseEntity(
            name = "Face Pull",
            description = "A cable exercise for rear delts and rotator cuff health.",
            muscleGroup = MuscleGroup.SHOULDERS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Set cable to upper chest height with rope attachment|Pull the rope toward your face, elbows out wide|Externally rotate at the top|Slowly return to start|Keep chest up throughout"
        ),
        ExerciseEntity(
            name = "Upright Row",
            description = "A compound exercise that trains the front and lateral deltoids.",
            muscleGroup = MuscleGroup.SHOULDERS.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Hold barbell or dumbbells in front of thighs|Pull the weight straight up to chin height|Lead with elbows, keeping them above the bar|Lower slowly back to starting position|Keep the movement controlled"
        ),

        // ARMS
        ExerciseEntity(
            name = "Barbell Bicep Curl",
            description = "The classic isolation exercise for building bicep mass.",
            muscleGroup = MuscleGroup.ARMS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Stand holding a barbell with an underhand grip|Keep elbows tucked at your sides|Curl the bar up to shoulder level|Squeeze biceps at the top|Lower with control, fully extending arms"
        ),
        ExerciseEntity(
            name = "Tricep Pushdown",
            description = "A cable isolation exercise for the triceps.",
            muscleGroup = MuscleGroup.ARMS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Stand at cable machine with bar attachment at upper chest|Keep elbows pinned at your sides|Push bar down until arms are fully extended|Squeeze triceps at the bottom|Slowly return to start"
        ),
        ExerciseEntity(
            name = "Hammer Curl",
            description = "A neutral-grip curl targeting biceps and brachialis.",
            muscleGroup = MuscleGroup.ARMS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Hold dumbbells with neutral grip (palms facing each other)|Keep elbows close to your body|Curl dumbbells up to shoulder level|Squeeze at the top|Lower with control"
        ),
        ExerciseEntity(
            name = "Skull Crushers",
            description = "A lying tricep extension exercise for building mass.",
            muscleGroup = MuscleGroup.ARMS.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Lie on a bench holding an EZ-bar or dumbbells|Arms extended perpendicular to the floor|Lower the weight toward your forehead by bending elbows|Extend arms back to start|Keep upper arms stationary throughout"
        ),
        ExerciseEntity(
            name = "Preacher Curl",
            description = "A bicep isolation exercise using the preacher bench for strict form.",
            muscleGroup = MuscleGroup.ARMS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Position arms on preacher bench pad|Hold barbell or dumbbells with underhand grip|Curl weight up to full contraction|Squeeze biceps at the top|Lower slowly to near full extension"
        ),

        // LEGS
        ExerciseEntity(
            name = "Back Squat",
            description = "The foundational lower body compound exercise.",
            muscleGroup = MuscleGroup.LEGS.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Position bar on upper back (traps)|Feet shoulder-width apart, toes slightly out|Squat down until thighs are parallel to floor|Drive through heels to stand|Keep chest up and knees tracking over toes"
        ),
        ExerciseEntity(
            name = "Romanian Deadlift",
            description = "A hip-hinge movement that targets the hamstrings and glutes.",
            muscleGroup = MuscleGroup.LEGS.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Stand holding the bar at hip height|Hinge at the hips, pushing them back|Lower the bar along the legs, feel hamstring stretch|Drive hips forward to return to standing|Keep back flat and core braced"
        ),
        ExerciseEntity(
            name = "Leg Press",
            description = "A machine-based compound leg exercise.",
            muscleGroup = MuscleGroup.LEGS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Sit in the leg press machine with feet shoulder-width apart|Lower the platform until knees reach 90 degrees|Press through heels to extend legs|Do not lock knees out completely at the top|Keep lower back pressed against the pad"
        ),
        ExerciseEntity(
            name = "Walking Lunges",
            description = "A unilateral leg exercise for quad and glute development.",
            muscleGroup = MuscleGroup.LEGS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Stand tall holding dumbbells at your sides|Step forward and lower rear knee toward the floor|Front thigh should be parallel to the floor|Drive through front heel to step forward|Alternate legs with each step"
        ),
        ExerciseEntity(
            name = "Leg Curl",
            description = "An isolation exercise for the hamstrings using a machine.",
            muscleGroup = MuscleGroup.LEGS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Lie face down on the leg curl machine|Position the pad just above the heels|Curl legs up as far as possible|Squeeze hamstrings at the top|Lower slowly to near full extension"
        ),
        ExerciseEntity(
            name = "Calf Raises",
            description = "An isolation exercise for the gastrocnemius and soleus muscles.",
            muscleGroup = MuscleGroup.LEGS.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Stand on the edge of a step or flat floor|Rise up on the balls of your feet|Hold the peak contraction for a moment|Lower heels slowly below the step level|Keep core braced and avoid bouncing"
        ),

        // CORE
        ExerciseEntity(
            name = "Plank",
            description = "A static core stability exercise.",
            muscleGroup = MuscleGroup.CORE.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Place forearms on the ground, elbows under shoulders|Extend legs behind you, toes on floor|Keep body in a straight line from head to heels|Brace your core and squeeze glutes|Hold the position for the target duration"
        ),
        ExerciseEntity(
            name = "Crunches",
            description = "A basic abdominal exercise.",
            muscleGroup = MuscleGroup.CORE.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Lie on your back, knees bent, feet flat|Place hands behind your head lightly|Curl shoulders up toward knees|Squeeze abs at the top|Lower slowly, don't fully release tension"
        ),
        ExerciseEntity(
            name = "Russian Twists",
            description = "A rotational core exercise targeting the obliques.",
            muscleGroup = MuscleGroup.CORE.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Sit with knees bent, feet lifted slightly|Lean back at 45 degrees|Hold hands together or a weight at chest|Rotate torso side to side|Touch the floor beside each hip each rep"
        ),
        ExerciseEntity(
            name = "Hanging Leg Raises",
            description = "An advanced core exercise for the lower abs.",
            muscleGroup = MuscleGroup.CORE.name,
            difficulty = Difficulty.ADVANCED.name,
            instructions = "Hang from a pull-up bar with a shoulder-width grip|Keep legs straight or slightly bent|Raise legs until parallel to the floor or higher|Lower with control, avoiding swinging|Use core, not momentum"
        ),
        ExerciseEntity(
            name = "Ab Wheel Rollout",
            description = "A challenging core exercise using an ab wheel.",
            muscleGroup = MuscleGroup.CORE.name,
            difficulty = Difficulty.ADVANCED.name,
            instructions = "Kneel on the floor holding the ab wheel|Roll the wheel forward, extending your body|Keep hips from sagging|Roll back to starting position using your abs|Start with partial range and progress"
        ),

        // CARDIO
        ExerciseEntity(
            name = "Running",
            description = "Steady-state cardiovascular exercise.",
            muscleGroup = MuscleGroup.CARDIO.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Start with a 5-minute warm-up walk|Maintain a comfortable conversational pace|Keep an upright posture, slight forward lean|Land mid-foot, not on your heel|Finish with a 5-minute cool-down walk"
        ),
        ExerciseEntity(
            name = "Jump Rope",
            description = "A high-intensity cardiovascular exercise.",
            muscleGroup = MuscleGroup.CARDIO.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Hold handles at hip height|Jump 1-2 inches off the ground|Land softly on the balls of your feet|Keep elbows close to your body|Start with 30-second intervals and build up"
        ),
        ExerciseEntity(
            name = "Burpees",
            description = "A full-body high-intensity exercise combining a squat, plank, and jump.",
            muscleGroup = MuscleGroup.CARDIO.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Start standing, drop hands to the floor|Jump feet back to a plank position|Perform a push-up (optional)|Jump feet to hands|Explode up with a jump and clap overhead"
        ),
        ExerciseEntity(
            name = "Cycling",
            description = "Low-impact cardiovascular exercise on a bike.",
            muscleGroup = MuscleGroup.CARDIO.name,
            difficulty = Difficulty.BEGINNER.name,
            instructions = "Adjust seat so leg is almost fully extended at bottom|Keep a smooth, circular pedaling motion|Maintain an upright or slightly forward position|Start at moderate resistance|Vary intensity with intervals for greater benefit"
        ),

        // FULL BODY
        ExerciseEntity(
            name = "Kettlebell Swing",
            description = "A powerful hip-hinge movement training the entire posterior chain.",
            muscleGroup = MuscleGroup.FULL_BODY.name,
            difficulty = Difficulty.INTERMEDIATE.name,
            instructions = "Stand feet shoulder-width apart with kettlebell between feet|Hinge at hips and grip the bell|Hike the bell back between legs|Drive hips forward explosively|Let the bell float to chest height, then repeat"
        )
    )
}
