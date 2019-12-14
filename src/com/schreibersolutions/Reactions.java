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
            System.out.printf("\n");
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

    private void replace (Reaction r, Ingredient source, ArrayList<Ingredient> target) {
//        for (Ingredient i: r.input_ingredients) {
//            if (i == source) {
                r.input_ingredients.remove(source);
                for (Ingredient new_i: target) {
                    boolean isAdded = false;
                    for (Ingredient old_i: r.input_ingredients) {
                        if (old_i.myType.compareTo(new_i.myType) == 0) {
                            old_i.myAmount = (old_i.getAmount() + new_i.getAmount());
                            old_i.multiplier = 1;
                            isAdded = true;
                        }
                    }
                    if (!isAdded) r.input_ingredients.add(new_i);
                }
//            }
//        }

    }
    public int getORERequired(Reaction r) {
        int ore = 0;
        Ingredient i= null;
        boolean nonOre = true;

        while (nonOre) {
            nonOre = false;
            for (int index = 0; index < r.input_ingredients.size(); index++) {
                i = r.input_ingredients.get(index);
                if (i.myType.compareTo("ORE") != 0) {
                    nonOre = true;
                    break;
                }
            }
            if (i.myType.compareTo("ORE") != 0) {
                replace(r, i, getReaction(i.myType, i.getAmount()).input_ingredients);
            }
            r.print();
        }

//        for (Ingredient i: r.input_ingredients) {
//            if (i.myType.compareTo("ORE")==0) {
//                ore += i.getAmount();
//            } else {
//
//                ore += getORERequired(getReaction(i.myType, i.getAmount()));
//            }
//        }
        return ore;
    }

    public int computeOREforFUEL() {
        int retval = 0;

        Reaction make_fuel = getFuelReaction();

        retval = getORERequired(make_fuel);

        return retval;
    }
}
