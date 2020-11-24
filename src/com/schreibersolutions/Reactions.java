package com.schreibersolutions;

import sun.management.jmxremote.SingleEntryRegistry;
import sun.rmi.server.InactiveGroupException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Reactions {
    boolean LOG = false;

    ArrayList<Reaction> reactions = new ArrayList<Reaction>();

    class Ingredient implements Comparable {
        String myType;
        long myAmount;
        long multiplier = 1;

        @Override
        public int compareTo(Object o) {
            return this.myType.compareTo(((Ingredient) o).myType);
        }

        public long getAmount() {
            return myAmount*multiplier;
        }

        Ingredient (String type, long amount) {
            myType = type;
            myAmount = amount;
        }

        Ingredient (String descriptor) {
            myType = descriptor.split(" ")[1];
            myAmount = Integer.parseInt(descriptor.split(" ")[0]);
        }

        boolean IsOre () {
            return (myType.compareTo("ORE") == 0);
        }
    }

    class Reaction {
        ArrayList<Ingredient> input_ingredients = new ArrayList<>();
        ArrayList<Ingredient> output_ingredients = new ArrayList<>();
        ArrayList<Ingredient> leftover_ingredients = new ArrayList<>();

        String base_input = "", base_output = "";
        long multiplier = 1;

        private void setInputOutput(String inputs, String outputs) {
            base_input = inputs;
            base_output = outputs;

            for (String i : inputs.split(", ")) {
                input_ingredients.add(new Ingredient(i));
            }
            output_ingredients.add(new Ingredient(outputs));
        }
        Reaction (String inputs, String outputs) {
            setInputOutput(inputs, outputs);
        }

        Reaction (String descriptor) {
            String input = descriptor.split(" => ")[0];
            String output = descriptor.split( " => ") [1];

            setInputOutput(input, output);
        }


        public void print() {
            if (!LOG) return;
            for (Ingredient i : input_ingredients) System.out.printf("%d %s ", i.getAmount(), i.myType);
            System.out.printf(" => ");
            for (Ingredient i : output_ingredients) System.out.printf("%d %s ", i.getAmount(), i.myType);
            System.out.printf(" <");
            Collections.sort(leftover_ingredients);
            for (Ingredient i : leftover_ingredients) System.out.printf("%d %s ", i.getAmount(), i.myType);
            System.out.printf(">\n");
        }

        public void increaseMultiplier(long m) {
            this.multiplier = m;
            for (Ingredient i : input_ingredients) {
                i.multiplier = m;
            }

            for (Ingredient i : output_ingredients) {
                i.multiplier = m;
            }

        }
        public void increaseMultiplier() {
            ++this.multiplier;

            for (Ingredient i : input_ingredients) {
                ++i.multiplier;
            }

            for (Ingredient i : output_ingredients) {
                ++i.multiplier;
            }
        }

        public Reaction clone() {
            return new Reaction(this.base_input, this.base_output);
        }
    }
    String reset_string = "";

    Reactions (String list) {
        String[] reaction_list = list.split("\n");
        reset_string = list;

        for (String s : reaction_list) {
            reactions.add(new Reaction(s));
        }
    }

    void reset() {
        String[] reaction_list = reset_string.split("\n");
        reactions.clear();

        for (String s : reaction_list) {
            reactions.add(new Reaction(s));
        }

    }
    public Reaction getReaction(String type, long amount) {
        Reaction r = getReaction(type);

        if (amount < 0) {
            System.out.println("Negative Amount Requested");
        }
        if (r.output_ingredients.get(0).getAmount() <  amount) {
            Reaction m_r = r.clone();

            long factor = (amount / r.output_ingredients.get(0).getAmount());
            m_r.increaseMultiplier(factor);
            while (m_r.output_ingredients.get(0).getAmount() < amount) {
                if (LOG) System.out.printf("Need %d, producing %d\n", amount, m_r.output_ingredients.get(0).getAmount());
                m_r.increaseMultiplier();
            }
            m_r.print();
            return m_r;
        } else {
            r.print();
            return r.clone();
        }
    }

    public void print() {
        for (Reaction local_r: reactions) {
            local_r.print();
        }
    }
    public Ingredient getIngredient(ArrayList<Ingredient> ingredients, String type) {
        for (Ingredient i: ingredients) {
            if (i.myType.compareTo(type) == 0) {
                return i;
            }
        }
        return new Ingredient(type, 0);
    }

    public Reaction getReaction(String type) {
        for (Reaction r : reactions) {
            if (r.output_ingredients.get(0).myType.compareTo(type) == 0) {
                return r.clone();
            }
        }
        return null;

    }

    public Reaction getFuelReaction() {
        Reaction f = getReaction("FUEL");
//        f.print();
        return f;
    }

    private void add(ArrayList<Ingredient> a, Ingredient b) {
        ArrayList<Ingredient> i = new ArrayList<Ingredient>();
        i.add(b);
        add(i,a);
    }

    private void add (ArrayList<Ingredient> a, ArrayList<Ingredient> b) {
        for (Ingredient new_i: a) {
            boolean isAdded = false;
            for (Ingredient old_i: b) {
                if (old_i.myType.compareTo(new_i.myType) == 0) {
                    old_i.myAmount = (old_i.getAmount() + new_i.getAmount());
                    old_i.multiplier = 1;
                    isAdded = true;
                }
            }
            if (!isAdded) b.add(new_i);
        }

    }
    private void delete (ArrayList<Ingredient> a, String type) {
        boolean found = false;
        Ingredient i = null;
        for (int index =0; index < a.size(); index++) {
            i = a.get(index);
            if (i.myType.compareTo(type) == 0) {
                found = true;
                break;
            }
        }
        if (found) {
            a.remove(i);
        }
    }

    private void replace (Reaction r, Ingredient source, ArrayList<Ingredient> target) {
//        for (Ingredient i: r.input_ingredients) {
//            if (i == source) {
                r.input_ingredients.remove(source);
                add(target,r.input_ingredients);
//            }
//        }

    }

    public void checkLeftovers(Reaction r) {

        for (int i = 0; i < r.input_ingredients.size(); i++) {
            Ingredient ingredient = r.input_ingredients.get(i);
            long leftoverAmount = getIngredient(r.leftover_ingredients, ingredient.myType).getAmount();
            if (leftoverAmount > 0) {
//                System.out.printf("==> Excess %s found (need %d, found %d)\n", ingredient.myType, ingredient.getAmount(), leftoverAmount);
            }
        }
    }

    public int findFewestInputIngredients(Reaction r) {
        int minInputIngredients = 999;

        for (int index = 0; index < r.input_ingredients.size(); index++) {
            Ingredient i = r.input_ingredients.get(index);
            if (!i.IsOre()) {
                Reaction i_r = getReaction(i.myType, i.getAmount());
                minInputIngredients = i_r.input_ingredients.size() < minInputIngredients ? i_r.input_ingredients.size() : minInputIngredients;
            }
        }
        return minInputIngredients;

    }

    public int findMostInputIngredients(Reaction r) {
        int maxInputIngredients = 0;

        for (int index = 0; index < r.input_ingredients.size(); index++) {
            Ingredient i = r.input_ingredients.get(index);
            if (!i.IsOre()) {
                Reaction i_r = getReaction(i.myType, i.getAmount());
                maxInputIngredients = i_r.input_ingredients.size() > maxInputIngredients ? i_r.input_ingredients.size() : maxInputIngredients;
            }
        }
        return maxInputIngredients;

    }
    public long getORERequired(Reaction r) {
        long ore = 0;
        Ingredient i= null;

        boolean nonOre = true;

        while (nonOre) {
            r.print();
            checkLeftovers(r);

            nonOre = false;
            for (int index = r.input_ingredients.size()-1; index >= 0 ; index--) {
//            for (int index = 0; index < r.input_ingredients.size(); index++) {

                i = r.input_ingredients.get(index);
//                if (!i.IsOre() && (getReaction(i.myType,i.getAmount()).input_ingredients.size() == findFewestInputIngredients(r)))  {
                if (!i.IsOre() )  {
                    nonOre = true;
                    break;
                }
            }

            if (!i.IsOre()) {
                long leftoverAmount = getIngredient(r.leftover_ingredients,i.myType).getAmount();

                // if I have enough leftover for what I need
                if (leftoverAmount >= i.getAmount()) {
                    delete(r.leftover_ingredients, i.myType);
                    add(r.leftover_ingredients, new Ingredient(i.myType, (leftoverAmount - i.getAmount())));
                    delete(r.input_ingredients, i.myType);
//                    System.out.printf("fulfilled with leftover %d %s\n", i.getAmount(), i.myType);
                } else {
                    // get the reaction to create what I need (needed amount less the leftover I have)
                    Reaction i_r = getReaction(i.myType, i.getAmount() - leftoverAmount);
                    ArrayList<Ingredient> i_i = i_r.input_ingredients;

                    delete(r.leftover_ingredients, i.myType);
                    if (i_r.output_ingredients.get(0).getAmount() > (i.getAmount() - leftoverAmount)) {
//                        System.out.printf("adding remainder %d %s\n", i_r.output_ingredients.get(0).getAmount() - (i.getAmount() - leftoverAmount), i.myType);
                        add(r.leftover_ingredients, new Ingredient(i.myType, i_r.output_ingredients.get(0).getAmount() - (i.getAmount() - leftoverAmount)));
                    }
                    replace(r, i, i_i);
                }
            }

        }
        ore = r.input_ingredients.get(0).getAmount();
        return ore;
    }

    public boolean isInIngredients(Ingredient i, ArrayList<Ingredient> list) {
        for (Ingredient which_ingredient: list) {
            if ((i.myType.compareTo(which_ingredient.myType) == 0) && (which_ingredient.getAmount() > 0)) {
                return true;
            }
        }
        return false;
    }


    public void reduceReactionToIngredients(Reaction r, ArrayList<Ingredient> basic_parts) {
        boolean nonReduced = true;
        Ingredient i=null;

        while (nonReduced) {
            r.print();
            nonReduced = false;
            for (int index = 0; index < r.input_ingredients.size(); index++) {
                i = r.input_ingredients.get(index);
                if (!isInIngredients(i, basic_parts)) {
                    nonReduced = true;
                    break;
                }
            }

            if (!i.IsOre()) {
                long leftoverAmount = getIngredient(r.leftover_ingredients, i.myType).getAmount();

                // if I have enough leftover for what I need
                if (leftoverAmount >= i.getAmount()) {
                    delete(r.leftover_ingredients, i.myType);
                    add(r.leftover_ingredients, new Ingredient(i.myType, (leftoverAmount - i.getAmount())));
                    delete(r.input_ingredients, i.myType);
//                    System.out.printf("fulfilled with leftover %d %s\n", i.getAmount(), i.myType);
                } else {
                    // get the reaction to create what I need (needed amount less the leftover I have)
                    Reaction i_r = getReaction(i.myType, i.getAmount() - leftoverAmount);
                    ArrayList<Ingredient> i_i = i_r.input_ingredients;

                    delete(r.leftover_ingredients, i.myType);
                    if (i_r.output_ingredients.get(0).getAmount() > (i.getAmount() - leftoverAmount)) {
//                        System.out.printf("adding remainder %d %s\n", i_r.output_ingredients.get(0).getAmount() - (i.getAmount() - leftoverAmount), i.myType);
                        add(r.leftover_ingredients, new Ingredient(i.myType, i_r.output_ingredients.get(0).getAmount() - (i.getAmount() - leftoverAmount)));
                    }
                    replace(r, i, i_i);
                    nonReduced = true;
                }
            }
        }

    }

    public long computeOREforFUEL() {
        long retval = 0;

        Reaction make_fuel = getFuelReaction();

        retval = getORERequired(make_fuel);

        return retval;
    }

    public void solver() {
        long ore_required;
        System.out.printf("Solver\n");

        Reaction make_fuel = getFuelReaction();

        ore_required = getORERequired(make_fuel);

        long available = 1000000000000l;
        long low_cycles = available/ore_required;
        long high_cycles = low_cycles * 4;

        long target = (low_cycles+high_cycles)/2;

        while ((target - low_cycles) > 0) {
            make_fuel = getReaction("FUEL", target);
            ore_required = getORERequired(make_fuel);

            System.out.printf("===> Requires %d ORE to make %d fuel (%d-%d)\n", ore_required, target, low_cycles, high_cycles);

            if (ore_required > available) {
                high_cycles = target;
            } else
            {
                low_cycles = target;
            }
            target = (high_cycles + low_cycles) / 2;
        }
        System.out.printf("===> Can make %d FUEL\n", target);

    }
}
