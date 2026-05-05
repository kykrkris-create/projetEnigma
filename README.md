#                                        _3N1GM4_.exe
> *La nuit est déjà tombée lorsque vous atteignez la vieille demeure...*

Une vieille demeure. Six énigmes. Une seule sortie. 
Bienvenue dans un escape game en Java où chaque mauvaise réponse te tue à sa façon, où la maison te parle 
avec un sourire malsain, et où même la victoire sonne comme une menace... 

---

## Le concept

Tu entres dans une vieille maison hantée. Tu n'es pas seul.
Pour t'échapper, tu vas devoir résoudre 6 énigmes — des charades, des QCM, des codes à trouver.
Chaque erreur te mène vers une fin unique et aleatoire ....

**Les énigmes du scénario :**

1.   Deux clés devant toi. Une seule ouvre la porte. Laquelle ?
2.   Tu te trompes ? Le tableau te demande pi avec 4 décimales. 
3.   Qui est le meurtrier de Scream 1 ? Réfléchis bien, tu n'as que deux essais :)
4.   Un mot de 7 lettres formé par les LEDs allumées. Indice : 2 lettres se réutilisent.
5.   Une charade. La cérémonie t'attend.
6.   Trois portes. Une victoire. Une mort. Une boucle infernale. Et c'est uniquement le destin qui choisit....

---

## 🛠️ Comment c'est fait

**Stack technique :**
- Java 17 + Swing
- Maven pour le build
- JUnit 5 pour les tests (22 tests unitaires)
- org.json pour parser les scénarios

## Pour entrer

cd Java_Project
mvn clean install
mvn exec:java

Ou F6 dans NetBeans.

mvn test    # si tu doutes de nous

---

## Avertissement final

> *"Tu n'as pas perdu. On a juste décidé que c'était fini pour toi :) " 
