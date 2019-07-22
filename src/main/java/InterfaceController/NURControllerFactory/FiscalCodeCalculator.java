package InterfaceController.NURControllerFactory;

public class FiscalCodeCalculator {
    public String calculateFC(String name, String surname, char sex, String birth) {
        String fiscalCode = "";
        String fcSurname = surname.replace(" ", "").toUpperCase();
        String fcName = name.replace(" ", "").toUpperCase();

        String consonants = consonants(fcSurname);
        String vowels = vowels(fcSurname);
        int consonantsLength = consonants.length();
        switch (consonantsLength) {
            case 0:
                if (vowels.length() > 2)
                    fiscalCode += vowels.substring(0, 3);
                else if (vowels.length() == 2)
                    fiscalCode += vowels + "x";
                else if (vowels.length() == 1)
                    fiscalCode += vowels + "xx";
                else
                    fiscalCode += "xxx";
                break;
            case 1:
                if (vowels.length() == 1)
                    fiscalCode += consonants + vowels + "x";
                else
                    fiscalCode += consonants + vowels.substring(0, 2);
                break;
            case 2:
                if (vowels.length() > 0)
                    fiscalCode += consonants + vowels.substring(0, 1);
                else
                    fiscalCode += consonants + "x";
                break;
            default:
                fiscalCode += consonants.substring(0, 3);
                break;
        }

        // NAME
        consonants = consonants(fcName);
        vowels = vowels(fcName);
        consonantsLength = consonants.length();
        switch (consonantsLength) {
            case 0:
                if (vowels.length() > 2)
                    fiscalCode += vowels.substring(0, 3);
                else if (vowels.length() == 2)
                    fiscalCode += vowels + "x";
                else if (vowels.length() == 1)
                    fiscalCode += vowels + "xx";
                else
                    fiscalCode += "xxx";
                break;
            case 1:
                if (vowels.length() == 1)
                    fiscalCode += consonants + vowels + "x";
                else
                    fiscalCode += consonants + vowels.substring(0, 2);
                break;
            case 2:
                if (vowels.length() > 0)
                    fiscalCode += consonants + vowels.substring(0, 1);
                else
                    fiscalCode += consonants + "x";
                break;
            case 3:
                fiscalCode += consonants;
                break;
            default:
                fiscalCode += consonants.substring(0, 1) + consonants.substring(2, 4);
                break;
        }


        /* Year */
        fiscalCode += birth.substring(2, 4);
        /* Month */
        int month = 0;
        if (birth.charAt(5) == '0')
            month = Integer.parseInt(birth.substring(6, 7));
        else
            month = Integer.parseInt(birth.substring(5, 7));
        switch (month) {
            case 1: {
                fiscalCode += "A";
                break;
            }
            case 2: {
                fiscalCode += "B";
                break;
            }
            case 3: {
                fiscalCode += "C";
                break;
            }
            case 4: {
                fiscalCode += "D";
                break;
            }
            case 5: {
                fiscalCode += "E";
                break;
            }
            case 6: {
                fiscalCode += "H";
                break;
            }
            case 7: {
                fiscalCode += "L";
                break;
            }
            case 8: {
                fiscalCode += "M";
                break;
            }
            case 9: {
                fiscalCode += "P";
                break;
            }
            case 10: {
                fiscalCode += "R";
                break;
            }
            case 11: {
                fiscalCode += "S";
                break;
            }
            case 12: {
                fiscalCode += "T";
                break;
            }
        }
        /* day */
        int day = 0;
        if (birth.charAt(8) == '0') {
            fiscalCode += '0';
            day = Integer.parseInt(birth.substring(9));
        } else
            day = Integer.parseInt(birth.substring(8));
        if (sex == 'M')
            fiscalCode += day;
        else {
            day += 40;
            fiscalCode += Integer.toString(day);
        }
        /* birth city */
        fiscalCode += "L781";

        /* Control char */
        fiscalCode = fiscalCode.toUpperCase();
        int evenSum = 0;
        for (int i = 1; i <= 13; i += 2) {
            switch (fiscalCode.charAt(i)) {
                case '0': {
                    evenSum += 0;
                    break;
                }
                case '1': {
                    evenSum += 1;
                    break;
                }
                case '2': {
                    evenSum += 2;
                    break;
                }
                case '3': {
                    evenSum += 3;
                    break;
                }
                case '4': {
                    evenSum += 4;
                    break;
                }
                case '5': {
                    evenSum += 5;
                    break;
                }
                case '6': {
                    evenSum += 6;
                    break;
                }
                case '7': {
                    evenSum += 7;
                    break;
                }
                case '8': {
                    evenSum += 8;
                    break;
                }
                case '9': {
                    evenSum += 9;
                    break;
                }
                case 'A': {
                    evenSum += 0;
                    break;
                }
                case 'B': {
                    evenSum += 1;
                    break;
                }
                case 'C': {
                    evenSum += 2;
                    break;
                }
                case 'D': {
                    evenSum += 3;
                    break;
                }
                case 'E': {
                    evenSum += 4;
                    break;
                }
                case 'F': {
                    evenSum += 5;
                    break;
                }
                case 'G': {
                    evenSum += 6;
                    break;
                }
                case 'H': {
                    evenSum += 7;
                    break;
                }
                case 'I': {
                    evenSum += 8;
                    break;
                }
                case 'J': {
                    evenSum += 9;
                    break;
                }
                case 'K': {
                    evenSum += 10;
                    break;
                }
                case 'L': {
                    evenSum += 11;
                    break;
                }
                case 'M': {
                    evenSum += 12;
                    break;
                }
                case 'N': {
                    evenSum += 13;
                    break;
                }
                case 'O': {
                    evenSum += 14;
                    break;
                }
                case 'P': {
                    evenSum += 15;
                    break;
                }
                case 'Q': {
                    evenSum += 16;
                    break;
                }
                case 'R': {
                    evenSum += 17;
                    break;
                }
                case 'S': {
                    evenSum += 18;
                    break;
                }
                case 'T': {
                    evenSum += 19;
                    break;
                }
                case 'U': {
                    evenSum += 20;
                    break;
                }
                case 'V': {
                    evenSum += 21;
                    break;
                }
                case 'W': {
                    evenSum += 22;
                    break;
                }
                case 'X': {
                    evenSum += 23;
                    break;
                }
                case 'Y': {
                    evenSum += 24;
                    break;
                }
                case 'Z': {
                    evenSum += 25;
                    break;
                }
            }
        }
        int oddSum = 0;
        for (int i = 0; i < 14; i += 2) {
            switch (fiscalCode.charAt(i)) {
                case '0': {
                    oddSum += 1;
                    break;
                }
                case '1': {
                    oddSum += 0;
                    break;
                }
                case '2': {
                    oddSum += 5;
                    break;
                }
                case '3': {
                    oddSum += 7;
                    break;
                }
                case '4': {
                    oddSum += 9;
                    break;
                }
                case '5': {
                    oddSum += 13;
                    break;
                }
                case '6': {
                    oddSum += 15;
                    break;
                }
                case '7': {
                    oddSum += 17;
                    break;
                }
                case '8': {
                    oddSum += 19;
                    break;
                }
                case '9': {
                    oddSum += 21;
                    break;
                }
                case 'A': {
                    oddSum += 1;
                    break;
                }
                case 'B': {
                    oddSum += 0;
                    break;
                }
                case 'C': {
                    oddSum += 5;
                    break;
                }
                case 'D': {
                    oddSum += 7;
                    break;
                }
                case 'E': {
                    oddSum += 9;
                    break;
                }
                case 'F': {
                    oddSum += 13;
                    break;
                }
                case 'G': {
                    oddSum += 15;
                    break;
                }
                case 'H': {
                    oddSum += 17;
                    break;
                }
                case 'I': {
                    oddSum += 19;
                    break;
                }
                case 'J': {
                    oddSum += 21;
                    break;
                }
                case 'K': {
                    oddSum += 2;
                    break;
                }
                case 'L': {
                    oddSum += 4;
                    break;
                }
                case 'M': {
                    oddSum += 18;
                    break;
                }
                case 'N': {
                    oddSum += 20;
                    break;
                }
                case 'O': {
                    oddSum += 11;
                    break;
                }
                case 'P': {
                    oddSum += 3;
                    break;
                }
                case 'Q': {
                    oddSum += 6;
                    break;
                }
                case 'R': {
                    oddSum += 8;
                    break;
                }
                case 'S': {
                    oddSum += 12;
                    break;
                }
                case 'T': {
                    oddSum += 14;
                    break;
                }
                case 'U': {
                    oddSum += 16;
                    break;
                }
                case 'V': {
                    oddSum += 10;
                    break;
                }
                case 'W': {
                    oddSum += 22;
                    break;
                }
                case 'X': {
                    oddSum += 25;
                    break;
                }
                case 'Y': {
                    oddSum += 24;
                    break;
                }
                case 'Z': {
                    oddSum += 23;
                    break;
                }
            }
        }
        int controlInteger = (evenSum + oddSum) % 26;
        String controlCharacter = "";
        switch (controlInteger) {
            case 0: {
                controlCharacter = "A";
                break;
            }
            case 1: {
                controlCharacter = "B";
                break;
            }
            case 2: {
                controlCharacter = "C";
                break;
            }
            case 3: {
                controlCharacter = "D";
                break;
            }
            case 4: {
                controlCharacter = "E";
                break;
            }
            case 5: {
                controlCharacter = "F";
                break;
            }
            case 6: {
                controlCharacter = "G";
                break;
            }
            case 7: {
                controlCharacter = "H";
                break;
            }
            case 8: {
                controlCharacter = "I";
                break;
            }
            case 9: {
                controlCharacter = "J";
                break;
            }
            case 10: {
                controlCharacter = "K";
                break;
            }
            case 11: {
                controlCharacter = "L";
                break;
            }
            case 12: {
                controlCharacter = "M";
                break;
            }
            case 13: {
                controlCharacter = "N";
                break;
            }
            case 14: {
                controlCharacter = "O";
                break;
            }
            case 15: {
                controlCharacter = "P";
                break;
            }
            case 16: {
                controlCharacter = "Q";
                break;
            }
            case 17: {
                controlCharacter = "R";
                break;
            }
            case 18: {
                controlCharacter = "S";
                break;
            }
            case 19: {
                controlCharacter = "T";
                break;
            }
            case 20: {
                controlCharacter = "U";
                break;
            }
            case 21: {
                controlCharacter = "V";
                break;
            }
            case 22: {
                controlCharacter = "W";
                break;
            }
            case 23: {
                controlCharacter = "X";
                break;
            }
            case 24: {
                controlCharacter = "Y";
                break;
            }
            case 25: {
                controlCharacter = "Z";
                break;
            }
        }
        fiscalCode += controlCharacter;
        return fiscalCode.toUpperCase();
    }

    private String consonants(String word) {
        word = word.toLowerCase();
        String consonants = "";
        for (char character : word.toCharArray()) {
            if (character != 'a' && character != 'e' && character != 'i'
                    && character != 'o' && character != 'u')
                consonants += character;
        }
        return consonants;
    }

    private String vowels(String word) {
        word = word.toLowerCase();
        String vowels = "";
        for (char character : word.toCharArray()) {
            if (character == 'a' || character == 'e' || character == 'i'
                    || character == 'o' || character == 'u')
                vowels += character;
        }
        return vowels;
    }
}


