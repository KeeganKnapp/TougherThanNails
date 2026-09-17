  package dev.keeganknapp.tougherthannails;

  import net.minecraft.resources.Identifier;
  import net.minecraft.world.entity.ai.attributes.AttributeModifier;

  public class ModModifiers {
      public static final Identifier ANTI_SPRINT_ID =
          Identifier.fromNamespaceAndPath("tougherthannails", "anti_sprint");

      // Vanilla adds SPEED_MODIFIER_SPRINTING = +0.3 ADD_MULTIPLIED_TOTAL while sprinting.
      // -0.23076923 as ADD_MULTIPLIED_TOTAL exactly neutralizes it:
      //   base * (1 + 0.3) * (1 - 0.23076923) == base
      public static final AttributeModifier CANCEL_SPRINT_SPEED = new AttributeModifier(
          ANTI_SPRINT_ID,
          -0.23076923,
          AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
      );
  }

