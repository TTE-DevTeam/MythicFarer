package de.dertoaster.mythicfarer.util;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.MovecraftRotation;
import net.countercraft.movecraft.util.MathUtils;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MathUtil extends MathUtils {

    static final Vector AXIS_X = new Vector(1,0,0);
    static final Vector AXIS_Y = new Vector(0,1,0);
    static final Vector AXIS_Z = new Vector(0,0,1);

    static Vector axisToVector(final Axis axis) {
        switch(axis) {
            case X: return AXIS_X;
            case Y: return AXIS_Y;
            case Z: return AXIS_Z;
        }
        return new Vector(0,0,0);
    }

    @Contract(
            pure = true
    )
    public static @NotNull MovecraftLocation rotateVec(@NotNull MovecraftRotation rotation, Axis axis, @NotNull MovecraftLocation movecraftLocation) {
        if (axis == Axis.Y) {
            return MathUtils.rotateVec(rotation, movecraftLocation);
        }

        Vector vector = new Vector(movecraftLocation.getX(), movecraftLocation.getY(), movecraftLocation.getZ());
        double angle = rotation == MovecraftRotation.CLOCKWISE ? Math.PI / 2 : -Math.PI / 2;
        vector = vector.rotateAroundAxis(axisToVector(axis), angle);
        return new MovecraftLocation(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    /** @deprecated */
    @Deprecated
    public static @NotNull double[] rotateVec(@NotNull MovecraftRotation rotation, Axis axis, double x, double z) {
        if (axis == Axis.Y) {
            return MathUtils.rotateVec(rotation, x, z);
        }

        Vector vector = new Vector(x, 0, z);
        double angle = rotation == MovecraftRotation.CLOCKWISE ? Math.PI / 2 : -Math.PI / 2;
        vector = vector.rotateAroundAxis(axisToVector(axis), angle);
        return new double[]{Math.round(vector.getX()), Math.round(vector.getY()), Math.round(vector.getZ())};
    }

    /** @deprecated */
    @Deprecated
    public static @NotNull double[] rotateVecNoRound(@NotNull MovecraftRotation r, Axis axis, double x, double z) {
        if (axis == Axis.Y) {
            return MathUtils.rotateVecNoRound(r, x, z);
        }

        Vector vector = new Vector(x, 0, z);
        double angle = r == MovecraftRotation.CLOCKWISE ? Math.PI / 2 : -Math.PI / 2;
        vector = vector.rotateAroundAxis(axisToVector(axis), angle);
        return new double[]{vector.getX(), vector.getY(), vector.getZ()};
    }

    public static final Random RNGESUS = new Random();

    public static float randomBetween(final float a, final float b) {
        return randomBetween(RNGESUS, a, b);
    }

    public static float randomBetween(final Random random, final float a, final float b) {
        if (a == b) {
            return a;
        }
        float aReal = Math.min(Math.abs(a), Math.abs(b));
        float bReal = Math.max(Math.abs(a), Math.abs(b));
        float result = random.nextFloat(bReal - aReal) + aReal;
        return result;
    }

    public static BlockFace getClosestFace(final Vector vectorIn, final BlockFace[] possibleFaces) {
        Vector vector = vectorIn.clone();
        if (vector.lengthSquared() == 0) return BlockFace.SELF;

        vector = vector.clone().normalize();

        BlockFace closest = BlockFace.NORTH;
        double maxDot = -Double.MAX_VALUE;

        for (BlockFace face : possibleFaces) {
            if (face == BlockFace.SELF) continue;

            Vector faceVector = new Vector(face.getModX(), face.getModY(), face.getModZ()).normalize();
            double dot = vector.dot(faceVector);
            if (dot > maxDot) {
                maxDot = dot;
                closest = face;
            }
        }

        return closest;
    }

    public static int getBlockSectionIndex(final int chunkRelativeX, final int sectionRelativeY, final int chunkRelativeZ) {
        return sectionRelativeY << 8 | chunkRelativeZ << 4 | chunkRelativeX;
    }

}
