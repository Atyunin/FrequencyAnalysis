import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Lab1 {

    static HashMap<Character, Character> keys;

    public static void main(String[] args) {

        String source;
        String pathname = "Text/SourceText.txt";
        String line;
        source = new String();

        try {

            File file = new File(pathname);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            line = reader.readLine();

            while (line != null) {

                line += '\n';
                source += line;
                line = reader.readLine();
            }

        } catch (Exception err) {
            err.printStackTrace();
        }

        String text = encryption(3, "повар", source);

        analysis_monogram(text, source);
        analysis_bigram(text, source);
    }

    private static void analysis_bigram(String source, String text) {

        //HashMap<String, Double> bigram_text_frecuency = new HashMap<>();
        //HashMap<String, Double> bigram_source_frecuency = new HashMap<>();

        HashMap<String, Double> bigram_text_frecuency10 = new HashMap<>();
        HashMap<String, Double> bigram_source_frecuency10 = new HashMap<>();

        HashMap<String, Integer> text_value = new HashMap<>();
        int count_bigrams = 0;

        for(int i = 0; i < text.length() - 1; i++) {

            if (!((text.charAt(i) >= 'а' && text.charAt(i) <= 'я') || (text.charAt(i) >= 'А' && text.charAt(i) <= 'Я')))
                continue;

            if (!((text.charAt(i + 1) >= 'а' && text.charAt(i + 1) <= 'я') || (text.charAt(i + 1) >= 'А' && text.charAt(i + 1) <= 'Я')))
                continue;

            char sym1 = (text.charAt(i) >= 'А' && text.charAt(i) <= 'Я') ? (char) (text.charAt(i) + ('а' - 'А')) : text.charAt(i);
            char sym2 =(text.charAt(i+1) >= 'А' && text.charAt(i+1) <= 'Я') ? (char) (text.charAt(i+1) + ('а' - 'А')) : text.charAt(i+1);

            if (!text_value.containsKey("" + sym1 + sym2)) {

                text_value.put("" + sym1 + sym2, 1);
                count_bigrams++;
            } else {

                text_value.put("" + sym1 + sym2,
                        text_value.get("" + sym1 + sym2) + 1);
                count_bigrams++;
            }
        }

        for (int i = 0; i < 10; i++) {

            String max_key = "";
            int max = 0;

            for (String key : text_value.keySet()) {

                if (text_value.get(key) > max) {

                    max_key = key;
                    max = text_value.get(key);
                }
            }

            text_value.remove(max_key);
            bigram_text_frecuency10.put(max_key, max * 100 / (double) count_bigrams);
        }

        HashMap<String, Integer> source_value = new HashMap<>();
        count_bigrams = 0;

        for(int i = 0; i < source.length() - 1; i++) {

            if (!((source.charAt(i) >= 'а' && source.charAt(i) <= 'я') || (source.charAt(i) >= 'А' && source.charAt(i) <= 'Я')))
                continue;

            if (!((source.charAt(i + 1) >= 'а' && source.charAt(i + 1) <= 'я') || (source.charAt(i + 1) >= 'А' && source.charAt(i + 1) <= 'Я')))
                continue;

            char sym1 = (source.charAt(i) >= 'А' && source.charAt(i) <= 'Я') ? (char) (source.charAt(i) + ('а' - 'А')) : source.charAt(i);
            char sym2 =(source.charAt(i+1) >= 'А' && source.charAt(i+1) <= 'Я') ? (char) (source.charAt(i+1) + ('а' - 'А')) : source.charAt(i+1);

            if (!source_value.containsKey("" + sym1 + sym2)) {

                source_value.put("" + sym1 + sym2, 1);
                count_bigrams++;
            } else {

                source_value.put("" + sym1 + sym2,
                        source_value.get("" + sym1 + sym2) + 1);
                count_bigrams++;
            }
        }

        for (int i = 0; i < 5; i++) {

            String max_key = "";
            int max = 0;

            for (String key : source_value.keySet()) {

                if (source_value.get(key) > max) {

                    max_key = key;
                    max = source_value.get(key);
                }
            }

            source_value.remove(max_key);
            bigram_source_frecuency10.put(max_key, max * 100 / (double) count_bigrams);
        }

        HashMap<String, String> bigram_map = new HashMap<>();

        //Сопаставление

        for (String key_source: bigram_source_frecuency10.keySet()) {

            double diff = 100;
            String key_encr = "";

            for (String key_text: bigram_text_frecuency10.keySet()) {

                if (Math.abs(bigram_text_frecuency10.get(key_text) - bigram_source_frecuency10.get(key_source)) < diff) {

                    diff = Math.abs(bigram_text_frecuency10.get(key_text) - bigram_source_frecuency10.get(key_source));
                    key_encr = key_text;
                }
            }

            bigram_map.put(key_source, key_encr);
        }

        //Расшифровка

        for (String key: bigram_map.keySet()) {


            char let1 = key.charAt(0);
            char let2 = bigram_map.get(key).charAt(0);

            if (keys.get(let1) != let2) {

                char let = keys.get(let1);
                keys.put(let1, let2);

                for (char ch: keys.keySet()) {

                    if (keys.get(ch) == let2 && ch != let1) {
                        keys.put(ch, let);
                        break;
                    }
                }
            }

            let1 = key.charAt(1);
            let2 = bigram_map.get(key).charAt(1);

            if (keys.get(let1) != let2) {

                char let = keys.get(let1);
                keys.put(let1, let2);

                for (char ch: keys.keySet()) {

                    if (keys.get(ch) == let2 && ch != let1) {
                        keys.put(ch, let);
                        break;
                    }
                }
            }
        }

        String new_text = "";

        for (int i = 0; i < source.length(); i++) {

            if ((source.charAt(i) >= 'а' && source.charAt(i) <= 'я')) {

                new_text += keys.get(source.charAt(i));
            } else if ((source.charAt(i) >= 'А' && source.charAt(i) <= 'Я')) {

                char sym = keys.get((char) (source.charAt(i) + ('а'-'А')));
                new_text += (char) (sym - ('а'-'А'));
            } else {

                new_text += source.charAt(i);
            }
        }

        System.out.println("Расшифровка биграммами:\n" + new_text);
    }

    static void analysis_monogram(String source, String text) {

        HashMap<Character, Double> frequency = new HashMap<>();
        HashMap<Character, Double> frequency_source = new HashMap<>();


        HashMap<Character, Integer> text_value = new HashMap<>();
        int count_letter = 0;

        /*for(int i = 0; i < text.length(); i++) {

            if ((text.charAt(i) >= 'а' && text.charAt(i) <= 'я')) {

                count_letter++;
                if (text_value.containsKey(text.charAt(i)))
                    text_value.put(text.charAt(i), text_value.get(text.charAt(i)) + 1);
                else
                    text_value.put(text.charAt(i), 1);

            } else if (text.charAt(i) >= 'А' && text.charAt(i) <= 'Я') {

                char letter = (char) (text.charAt(i) + ('а' - 'А'));

                count_letter++;
                if (text_value.containsKey(letter))
                    text_value.put(letter, text_value.get(letter) + 1);
                else
                    text_value.put(letter, 1);
            }
        }

        for (char l = 'а'; l <= 'я'; l++) {

            if (text_value.containsKey(l))
                frequency.put(l, (text_value.get(l) * 100.0 / count_letter));
        }*/

        frequency.put('а', 8.01);
        frequency.put('б', 1.59);
        frequency.put('в', 4.54);
        frequency.put('г', 1.70);
        frequency.put('д', 2.98);
        frequency.put('е', 8.45);
        frequency.put('ж', 0.94);
        frequency.put('з', 1.65);
        frequency.put('и', 7.35);
        frequency.put('й', 1.21);
        frequency.put('к', 3.49);
        frequency.put('л', 4.40);
        frequency.put('м', 3.21);
        frequency.put('н', 6.70);
        frequency.put('о', 10.97);
        frequency.put('п', 2.81);
        frequency.put('р', 4.73);
        frequency.put('с', 5.47);
        frequency.put('т', 6.26);
        frequency.put('у', 2.62);
        frequency.put('ф', 0.26);
        frequency.put('х', 0.97);
        frequency.put('ц', 0.48);
        frequency.put('ч', 1.44);
        frequency.put('ш', 0.73);
        frequency.put('щ', 0.36);
        frequency.put('ъ', 0.04);
        frequency.put('ы', 1.90);
        frequency.put('ь', 1.74);
        frequency.put('э', 0.32);
        frequency.put('ю', 0.64);
        frequency.put('я', 2.01);


        HashMap<Character, Integer> source_value = new HashMap<>();
        count_letter = 0;

        for(int i = 0; i < source.length(); i++) {

            if ((source.charAt(i) >= 'а' && source.charAt(i) <= 'я')) {

                count_letter++;
                if (source_value.containsKey(source.charAt(i)))
                    source_value.put(source.charAt(i), source_value.get(source.charAt(i)) + 1);
                else
                    source_value.put(source.charAt(i), 1);

            } else if (source.charAt(i) >= 'А' && source.charAt(i) <= 'Я') {

                char letter = (char) (source.charAt(i) + ('а' - 'А'));

                count_letter++;
                if (source_value.containsKey(letter))
                    source_value.put(letter, source_value.get(letter) + 1);
                else
                    source_value.put(letter, 1);
            }
        }

        for (char l = 'а'; l <= 'я'; l++) {

            if (source_value.containsKey(l))
            frequency_source.put(l, (source_value.get(l) * 100.0 / count_letter));
        }


        keys = new HashMap<>();

        for (char key_source: frequency_source.keySet()) {

            double diff = 100;
            char key_encr = ' ';

            for (char key_text: frequency.keySet()) {

                if (Math.abs(frequency.get(key_text) - frequency_source.get(key_source)) < diff) {

                    diff = Math.abs(frequency.get(key_text) - frequency_source.get(key_source));
                    key_encr = key_text;
                }
            }

            keys.put(key_source, key_encr);
            frequency.remove(key_encr);
        }

        String new_text = "";

        for (int i = 0; i < source.length(); i++) {

            if ((source.charAt(i) >= 'а' && source.charAt(i) <= 'я')) {

                new_text += keys.get(source.charAt(i));
            } else if ((source.charAt(i) >= 'А' && source.charAt(i) <= 'Я')) {

                char sym = keys.get((char) (source.charAt(i) + ('а'-'А')));
                new_text += (char) (sym - ('а'-'А'));
            } else {

                new_text += source.charAt(i);
            }
        }

        System.out.println("Расшифровка монограмами:\n" + new_text);
    }

    static String encryption(int shift, String word, String source) {

        char new_alphabet [] = new char[32];

        for (char s = 'а'; s <= 'я'; s++)
            new_alphabet[s - 'а'] = s;

        for (int i = 0; i < 32; i++) {
            new_alphabet[i] = (char) (new_alphabet[i] + shift);
            if (new_alphabet[i] > 'я')
                new_alphabet[i] -= 32;
        }

        for (int i = 0; i < word.length(); i++) {

            char let = new_alphabet[i];
            new_alphabet[i] = word.charAt(i);
            for (int j = i + 1; j < 32; j++)
                if (new_alphabet[j] == word.charAt(i))
                    new_alphabet[j] = let;
        }

        for (int i = 0; i < 32; i++)
            System.out.println("" + ((char) (i + 'а')) + " = " + new_alphabet[i]);

        String new_text = "";


        for (int i = 0; i < source.length(); i++) {

            if ((source.charAt(i) >= 'а' && source.charAt(i) <= 'я')) {

                new_text += new_alphabet[source.charAt(i) - 'а'];
            } else if ((source.charAt(i) >= 'А' && source.charAt(i) <= 'Я')) {

                new_text += (char) (new_alphabet[source.charAt(i) - 'а' + ('а'-'А')] - ('а'-'А'));
            } else {

                new_text += source.charAt(i);
            }
        }

        return new_text;
    }
}
