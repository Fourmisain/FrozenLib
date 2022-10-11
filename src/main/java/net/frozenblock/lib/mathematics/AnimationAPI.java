package net.frozenblock.lib.mathematics;

import java.awt.geom.Point2D;

public interface AnimationAPI {
    /**
     * ANIMATION API
     * <p>
     * This class is used to make animations with easings
     * <p>
     * Only for FrozenBlock Modders, ALL RIGHTS RESERVED
     * <p>
     * Defining a point A(x,y) & B(x,y) you can create an animation between those two points ( A.getY() won't affect the animation).
     * Learn more at https://github.com/LIUKRAST/AnimationAPI/blob/main/README.md
     *
     * @author LiukRast (2021-2022)
     * @since 4.0
     */

    private float relativeX(Point2D a, Point2D b, float x) {
        return (float) ((x - a.getX()) / (b.getX() - a.getX()));
    }

    /**
     * Generates a "random" number depending on another number.
     *
     * @deprecated Use seed() instead of this!
     **/
    @Deprecated
    default float rawSeed(float seed) {
        float f = (float) Math.pow(Math.PI, 3);
        float linear = (seed + f) * f;
        float flat = (float) Math.floor(linear);
        return linear - flat;
    }

    /**
     * Executes {@link #rawSeed(float)} multiple times to make the number look more "random"
     **/
    default float seed(float seed) {
        return rawSeed(rawSeed(rawSeed(seed)));
    }

    /**
     * Convert a 2D position with a seed in a resulting seed
     **/
    default float seed2D(Point2D seed2d, float seed) {
        return rawSeed((float) seed2d.getX()) * rawSeed((float) seed2d.getX()) *
                rawSeed(seed);
    }

    default float legAnimation(float base, float range, float frequency,
                               float limbAngle, float limbDistance,
                               boolean inverted) {
        float baseRange = 1.4f;
        float baseFrequency = 0.6662f;
        float wave = (float) Math.sin(limbAngle * (baseFrequency * frequency)) *
                (baseRange * range) * limbDistance;
        if (inverted) {
            return base + wave;
        } else {
            return base - wave;
        }
    }

    default float legAnimation(float base, float range, float frequency,
                               float limbAngle, float limbDistance) {
        return legAnimation(base, range, frequency, limbAngle, limbDistance,
                false);
    }

    default float legAnimation(float base, float limbAngle, float limbDistance,
                               boolean inverted) {
        return legAnimation(base, 1, 1, limbAngle, limbDistance, inverted);
    }

    default float legAnimation(float base, float limbAngle,
                               float limbDistance) {
        return legAnimation(base, limbAngle, limbDistance, false);
    }


    /**
     * SINE EASING - Generated using Math.sin()
     */
    default float SineEaseIn(Point2D a, Point2D b, float x) {
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (1 - (float) Math.cos(Math.PI * (relativeX(a, b, x) / 2)));
        }
    }

    default float SineEaseOut(Point2D a, Point2D b, float x) {
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    ((float) Math.sin(Math.PI * (relativeX(a, b, x) / 2)));
        }
    }

    default float SineEaseInOut(Point2D a, Point2D b, float x) {
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (0.5f -
                    ((float) Math.cos(Math.PI * relativeX(a, b, x)) / 2));
        }
    }
    // -------------------------------------------------------

    /**
     * POLYNOMIAL EASING - Generated by elevating x at a "c" value
     */
    default float PolyEaseIn(Point2D a, Point2D b, float x, float c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * ((float) Math.pow(relativeX(a, b, x), c));
        }
    }

    default float PolyEaseOut(Point2D a, Point2D b, float x, float c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (1 - (float) Math.pow(-(relativeX(a, b, x) - 1), c));
        }
    }

    default float PolyEaseInOut(Point2D a, Point2D b, float x, float c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            if (x < (b.getX() - a.getX()) / 2) {
                return (float) b.getY() * (float) (Math.pow(2, c - 1) *
                        Math.pow(relativeX(a, b, x), c));
            } else {
                return (float) b.getY() * (float) (1 -
                        Math.pow(2 - 2 * relativeX(a, b, x), c) / 2);
            }
        }
    }
    // -------------------------------------------------------

    /**
     * QUADRATIC EASING - Generated using Poly and assuming c = 2
     */
    default float QuadraticEaseIn(Point2D a, Point2D b, float x) {
        return PolyEaseIn(a, b, x, 2);
    }

    default float QuadraticEaseOut(Point2D a, Point2D b, float x) {
        return PolyEaseOut(a, b, x, 2);
    }

    default float QuadraticEaseInOut(Point2D a, Point2D b, float x) {
        return PolyEaseInOut(a, b, x, 2);
    }
    // -------------------------------------------------------

    /**
     * CUBIC EASING - Generated using Poly and assuming c = 3
     */
    default float CubicEaseIn(Point2D a, Point2D b, float x) {
        return PolyEaseIn(a, b, x, 3);
    }

    default float CubicEaseOut(Point2D a, Point2D b, float x) {
        return PolyEaseOut(a, b, x, 3);
    }

    default float CubicEaseInOut(Point2D a, Point2D b, float x) {
        return PolyEaseInOut(a, b, x, 3);
    }
    // -------------------------------------------------------

    /**
     * QUARTIC EASING - Generated using Poly and assuming c = 4
     */
    default float QuarticEaseIn(Point2D a, Point2D b, float x) {
        return PolyEaseIn(a, b, x, 4);
    }

    default float QuarticEaseOut(Point2D a, Point2D b, float x) {
        return PolyEaseOut(a, b, x, 4);
    }

    default float QuarticEaseInOut(Point2D a, Point2D b, float x) {
        return PolyEaseInOut(a, b, x, 4);
    }
    // -------------------------------------------------------

    /**
     * QUINTIC EASING - Generated using Poly and assuming c = 5
     */
    default float QuinticEaseIn(Point2D a, Point2D b, float x) {
        return PolyEaseIn(a, b, x, 5);
    }

    default float QuinticEaseOut(Point2D a, Point2D b, float x) {
        return PolyEaseOut(a, b, x, 5);
    }

    default float QuinticEaseInOut(Point2D a, Point2D b, float x) {
        return PolyEaseInOut(a, b, x, 5);
    }
    // -------------------------------------------------------

    /**
     * EXPONENTIAL EASING - Generated by 2^x
     */
    default float ExpoEaseIn(Point2D a, Point2D b, float x) {
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (float) Math.pow(2, (10 * relativeX(a, b, x)) - 10);
        }
    }

    default float ExpoEaseOut(Point2D a, Point2D b, float x) {
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (1 - (float) Math.pow(2, -10 * relativeX(a, b, x)));
        }
    }

    default float ExpoEaseInOut(Point2D a, Point2D b, float x) {
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            if (x < (b.getX() - a.getX()) / 2) {
                return (float) b.getY() *
                        (float) Math.pow(2, (20 * relativeX(a, b, x)) - 10) / 2;
            } else {
                return (float) b.getY() * (float) (2 -
                        Math.pow(2, 10 - (20 * relativeX(a, b, x)))) / 2;
            }
        }
    }
    // -------------------------------------------------------

    /**
     * CICRULAR EASING - Uses Roots and Powers to make curves
     */
    default float CircEaseIn(Point2D a, Point2D b, float x, int roundness) {
        if (roundness < 0) {
            System.out.println("Animation API error - roundness must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) (1 -
                    Math.pow(1 - Math.pow(relativeX(a, b, x), roundness),
                            1 / (float) roundness));
        }
    }

    default float CircEaseOut(Point2D a, Point2D b, float x, int roundness) {
        if (roundness < 0) {
            System.out.println("Animation API error - roundness must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) Math.pow(
                    1 - Math.pow(relativeX(a, b, x) - 1, roundness),
                    1 / (float) roundness);
        }
    }

    default float CircEaseInOut(Point2D a, Point2D b, float x, int roundness) {
        if (roundness < 0) {
            System.out.println("Animation API error - roundness must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            if (x < (b.getX() - a.getX()) / 2) {
                return (float) b.getY() * (float) (1 - Math.pow(
                        1 - Math.pow(2 * relativeX(a, b, x), roundness),
                        1 / (float) roundness)) / 2;
            } else {
                return (float) b.getY() * (float) (Math.pow(
                        1 - Math.pow(-2 * relativeX(a, b, x) + 2, roundness),
                        1 / (float) roundness) + 1) / 2;
            }
        }
    }

    default float CircEaseIn(Point2D a, Point2D b, float x) {
        return CircEaseIn(a, b, x, 2);
    }

    default float CircEaseOut(Point2D a, Point2D b, float x) {
        return CircEaseOut(a, b, x, 2);
    }

    default float CircEaseInOut(Point2D a, Point2D b, float x) {
        return CircEaseInOut(a, b, x, 2);
    }
    // -------------------------------------------------------

    /**
     * ELASTIC EASING - Generated by Cosine and a variable "c" of the curves intensity
     */
    default float ElasticEaseIn(Point2D a, Point2D b, float x, int c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (float) (Math.cos(2 * Math.PI * c * relativeX(a, b, x)) *
                            relativeX(a, b, x));
        }
    }

    default float ElasticEaseOut(Point2D a, Point2D b, float x, int c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) (1 -
                    (Math.cos(2 * Math.PI * c * relativeX(a, b, x)) *
                            (1 - relativeX(a, b, x))));
        }
    }

    default float ElasticEaseInOut(Point2D a, Point2D b, float x, int c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) (relativeX(a, b, x) +
                    (Math.sin(2 * Math.PI * c * relativeX(a, b, x)) *
                            Math.sin(Math.PI * relativeX(a, b, x))));
        }
    }

    // Same Equations but automaticly defines
    default float ElasticEaseIn(Point2D a, Point2D b, float x) {
        int c = (int) (b.getX() - a.getX());
        return ElasticEaseIn(a, b, x, c);
    }

    default float ElasticEaseOut(Point2D a, Point2D b, float x) {
        int c = (int) (b.getX() - a.getX());
        return ElasticEaseOut(a, b, x, c);
    }

    default float ElasticEaseInOut(Point2D a, Point2D b, float x) {
        int c = (int) (b.getX() - a.getX());
        return ElasticEaseInOut(a, b, x, c);
    }
    // -------------------------------------------------------

    /**
     * BOUNCE EASING - Generated by an elastic absoluted
     */
    default float BounceEaseIn(Point2D a, Point2D b, float x, int c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) Math.abs(
                    Math.cos(2 * Math.PI * c * relativeX(a, b, x)) *
                            relativeX(a, b, x));
        }
    }

    default float BounceEaseOut(Point2D a, Point2D b, float x, int c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) (1 - Math.abs(
                    Math.cos(2 * Math.PI * c * relativeX(a, b, x)) *
                            (1 - relativeX(a, b, x))));
        }
    }

    default float BounceEaseInOut(Point2D a, Point2D b, float x, int c) {
        if (c < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() * (float) (relativeX(a, b, x) + Math.abs(
                    Math.sin(2 * Math.PI * c * relativeX(a, b, x)) *
                            Math.sin(Math.PI * relativeX(a, b, x))));
        }
    }

    // Same Equations but automatically defines c
    default float BounceEaseIn(Point2D a, Point2D b, float x) {
        int c = (int) (b.getX() - a.getX());
        return ElasticEaseIn(a, b, x, c);
    }

    default float BounceEaseOut(Point2D a, Point2D b, float x) {
        int c = (int) (b.getX() - a.getX());
        return ElasticEaseOut(a, b, x, c);
    }

    default float BounceEaseInOut(Point2D a, Point2D b, float x) {
        int c = (int) (b.getX() - a.getX());
        return ElasticEaseInOut(a, b, x, c);
    }
    // -------------------------------------------------------

    /**
     * BACK EASING - Generates a curve that comes back a little at the end (defined by an amount a >= 0)
     */
    default float BackEaseIn(Point2D a, Point2D b, float x, float c1) {
        float c2 = c1 + 1;
        if (c1 < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (float) (c2 * Math.pow(relativeX(a, b, x), 3) -
                            c1 * Math.pow(relativeX(a, b, x) - 1, 2));
        }
    }

    default float BackEaseOut(Point2D a, Point2D b, float x, float c1) {
        float c2 = c1 + 1;
        if (c1 < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            return (float) b.getY() *
                    (float) (1 + c2 * Math.pow(relativeX(a, b, x) - 1, 3) +
                            c1 * Math.pow(relativeX(a, b, x) - 1, 2));
        }
    }

    default float BackEaseInOut(Point2D a, Point2D b, float x, float c1) {
        float c2 = c1 + 1;
        float c3 = c1 * 1.525f;
        if (c1 < 0) {
            System.out.println("Animation API error - c must be >= 0");
            return (float) Math.random();
        }
        if (x < a.getX()) {
            return 0; // before animation defining the eq as 0
        } else if (x > b.getX()) {
            return (float) b.getY(); // after animation defining the eq as b's Y
        } else {
            if (x < (b.getX() - a.getX()) / 2) {
                return (float) b.getY() *
                        (float) (Math.pow(2 * relativeX(a, b, x), 2) *
                                ((c3 + 1) * 2 * relativeX(a, b, x) - c3)) / 2;
            } else {
                return (float) b.getY() * (float) (
                        Math.pow(2 * relativeX(a, b, x) - 2, 2) *
                                ((c3 + 1) * (2 * relativeX(a, b, x) - 2) + c3) +
                                2) / 2;
            }
        }
    }

    // Same method but automatically defines c1
    default float BackEaseIn(Point2D a, Point2D b, float x) {
        return BackEaseIn(a, b, x, 1.70158f);
    }

    default float BackEaseOut(Point2D a, Point2D b, float x) {
        return BackEaseOut(a, b, x, 1.70158f);
    }

    default float BackEaseInOut(Point2D a, Point2D b, float x) {
        return BackEaseInOut(a, b, x, 1.70158f);
    }
    // -------------------------------------------------------

    /**
     * LOOP SYSTEM
     * Loop: defines A & B and always repeat between these two values
     * Boomerang: creates a loop but instead of repeating it from start, it comes back and THEN loop
     */
    private float line(Point2D a, Point2D b, float x) {
        return (float) (relativeX(a, b, x) * (b.getY() - a.getY()) + a.getY());
    }

    private float flat(Point2D a, Point2D b, float x) {
        return (float) (Math.floor(relativeX(a, b, x)) * (b.getY() - a.getY()) +
                a.getY());
    }

    private float flat2(Point2D a, Point2D b, float x) {
        return (float) (
                2 * Math.floor(relativeX(a, b, x) / 2) * (b.getY() - a.getY()) +
                        a.getY());
    }

    private float inverse(Point2D a, Point2D b, float x) {
        return (float) (flat(a, b, x) + b.getY() - line(a, b, x));
    }

    // BOOMERANG
    default float boomerang(Point2D a, Point2D b, float x) {
        return line(a, b, x) - flat2(a, b, x) + a.getY() < b.getY() ?
                (float) (line(a, b, x) - flat2(a, b, x) + a.getY()) :
                inverse(a, b, x);
    }

    // LOOP
    default float loop(Point2D a, Point2D b, float x) {
        return (float) (line(a, b, x) - flat(a, b, x) + a.getY());
    }
    // -------------------------------------------------------

    /*
     * Animation API - LiukRast, ALL RIGHTS RESERVED (2021-2022)
     */
}