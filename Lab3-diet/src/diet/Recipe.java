package diet;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe implements NutritionalElement {
	private final String name;
	private final Food food;

	private double weight = 0.0;

	private List<Ingredient> ingredients = new LinkedList<>();  


	Recipe(String name, Food food) {
		this.name = name;
		this.food = food;
	}

	public static class Ingredient {
		final NutritionalElement ne;
		final double qty;

		public Ingredient(NutritionalElement ne, double qty) {
			this.ne = ne;
			this.qty = qty;
		}
	}

	
	/**
	 * Adds the given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 * @return the same Recipe object, it allows method chaining.
	 */
	public Recipe addIngredient(String material, double quantity) {
		NutritionalElement ne = food.getRawMaterial(material);
		Ingredient ing = new Ingredient(ne, quantity);
		ingredients.add(ing);
		weight += ing.qty;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public double getCalories() {
		double res = 0.0;
		for (Ingredient ing : ingredients) {
			res += ing.ne.getCalories() * ing.qty / 100;
		}
		return res * 100 / weight;
	}
	

	@Override
	public double getProteins() {
		double res = 0.0;
		for (Ingredient ing : ingredients) {
			res += ing.ne.getProteins() * ing.qty / 100;
		}
		return res * 100 / weight;
	}

	@Override
	public double getCarbs() {
		double res = 0.0;
		for (Ingredient ing : ingredients) {
			res += ing.ne.getCarbs() * ing.qty / 100;
		}
		return res * 100 / weight;
	}

	@Override
	public double getFat() {
		double res = 0.0;
		for (Ingredient ing : ingredients) {
			res += ing.ne.getFat() * ing.qty / 100;
		}
		return res * 100 / weight;
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expresses nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Ingredient ing : ingredients) {
			sb.append(ing.ne.getName());
			sb.append(" : ");
			sb.append(ing.qty);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}