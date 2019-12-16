package com.schreibersolutions;

import sun.rmi.server.InactiveGroupException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Reactions {
    ArrayList<Reaction> reactions = new ArrayList<Reaction>();

    class Ingredient {
        String myType;
        int myAmount;
        int multiplier = 1;

        public int getAmount() {
            return myAmount*multiplier;
        }

        Ingredient (String type, int amount) {
            myType = type;
            myAmount = amount;
        }

        Ingredient (String descriptor) {
            myType = descriptor.split(" ")[1];
            myAmount = Integer.parseInt(descriptor.split(" ")[0]);
        }

    }

    class Reaction {
        ArrayList<Ingredient> input_ingredients = new ArrayList<>();
        ArrayList<Ingredient> output_ingredients = new ArrayList<>();
        ArrayList<Ingredient> leftover_ingredients = new ArrayList<>();

        String base_input = "", base_output = "";
        int multiplier = 1;

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
            for (Ingredient i : input_ingredients) System.out.printf("%d %s ", i.getAmount(), i.myType);
            System.out.printf(" => ");
            for (Ingredient i : output_ingredients) System.out.printf("%d %s ", i.getAmount(), i.myType);
            System.out.printf(" <");
            for (Ingredient i : leftover_ingredients) System.out.printf("%d %s ", i.getAmount(), i.myType);
            System.out.printf(">\n");
        }

        public void increaseMultiplier() {
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

    Reactions (String list) {
        String[] reaction_list = list.split("\n");

        for (String s : reaction_list) {
            reactions.add(new Reaction(s));
        }
    }

    public Reaction getReaction(String type, int amount) {
        Reaction r = getReaction(type);

        if (amount < 0) {
            System.out.println("Negative Amount Requested");
        }
        if (r.output_ingredients.get(0).getAmount() <  amount) {
            Reaction m_r = r.clone();

            while (m_r.output_ingredients.get(0).getAmount() < amount) {
                m_r.increaseMultiplier();
            }
            m_r.print();
            return m_r;
        } else {
            r.print();
            return r;
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
                return r;
            }
        }
        return null;

    }

    public Reaction getFuelReaction() {
        Reaction f = getReaction("FUEL");
        f.print();
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
    public int getORERequired(Reaction r) {
        int ore = 0;
        Ingredient i= null;
        int i_pos = 0;
        boolean nonOre = true;
        boolean onlyORELeft = true;
        boolean computeORE = false;

        while (nonOre) {
            nonOre = false;
            for (int index = i_pos; index < r.input_ingredients.size(); index++) {
                i = r.input_ingredients.get(index);
                if (i.myType.compareTo("ORE") != 0) {
                    nonOre = true;
                    i_pos = index;
                    break;
                }
            }
            if (i.myType.compareTo("ORE") != 0) {
                int leftoverAmount = getIngredient(r.leftover_ingredients,i.myType).getAmount();
                if (leftoverAmount >= i.getAmount()) {
                    delete(r.leftover_ingredients, i.myType);
                    add(r.leftover_ingredients, new Ingredient(i.myType, (leftoverAmount - i.getAmount())));
                    delete(r.input_ingredients, i.myType);
                    System.out.printf("fulfilled with leftover %d %s\n", i.getAmount(), i.myType);
                    onlyORELeft = false;
                    ++i_pos;
                } else {
                    Reaction i_r = getReaction(i.myType, i.getAmount() - leftoverAmount);
                    ArrayList<Ingredient> i_i = i_r.input_ingredients;
                    //                if ( (i_i.get(0).myType.compareTo(("ORE")) != 0)
                    //                        || ((i_i.get(0).myType.compareTo("ORE") == 0) && computeORE) ) {
                    delete(r.leftover_ingredients, i.myType);
                    if (i_r.output_ingredients.get(0).getAmount() > (i.getAmount() - leftoverAmount)) {
                        System.out.printf("adding remainder %d %s\n", i_r.output_ingredients.get(0).getAmount() - (i.getAmount() - leftoverAmount), i.myType);
                        add(r.leftover_ingredients, new Ingredient(i.myType, i_r.output_ingredients.get(0).getAmount() - (i.getAmount() - leftoverAmount)));
                    }
                    replace(r, i, i_i);
                    i_pos += i_i.size() - 1;
                    onlyORELeft = false;
                    //                } else {
                    //                    ++i_pos;
                    //                }
                }
            }
            r.print();

            if (i_pos >= r.input_ingredients.size()) {
                i_pos = 0;
                if (onlyORELeft) {
                    computeORE = true;
                }
                onlyORELeft = true;
            }
        }

        return ore;
    }

    public int computeOREforFUEL() {
        int retval = 0;

        Reaction make_fuel = getFuelReaction();

        retval = getORERequired(make_fuel);

        return retval;
    }
}
