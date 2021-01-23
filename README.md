# Intra-class

## Basic
- CA (Classified Attributes) - Number of **classified** attributes of a class. **classified** is computed through heuristics (pattern matching, e.g. password, token, etc.)
- ICA (Indirectly Classified Attributes) - Number of attributes of a class whose value is influenced (data-flow) by a **classified** attribute.
- CM (Classified Methods) - Number of **classified** methods of a class. A method is **classified** if it uses (read/write) **classified** attributes and/or using heuristics (pattern matching, e.g., validatePassword, generateToken).
  - CWM (Classified Write Method) - Number of CM that writes CA.
  - CRM (Classified Read Method) - Number of CM that reads CA.

## Encapsulation
- CIVA (Classified Instance Variables Accessibility) - Ratio of non-private non-static CA out of the total of CA.
- CCVA (Classified Class Variables Accessibility) - Ratio of non-private static CA out of the total of CA.
- CMA (Classified Method Accessibility) - Ratio of non-private CM out of the total of CM.
- RP (Reflecion Package) - true/false use of java.reflection

## Cohesion
- CMR (CM Ratio) - Ratio of CM out of number of total methods
  - CWMR (CWM Ratio) - Ratio of CWM out of total CM
  - CRMR (CRM Ratio) - Ratio of CRM out of total CM
- CAI (Classified Attribute Interactions) - Ratio of sum of CM per CA out of the product of all CM and all CA
- CMAI (Classified Mutator-Attribute Interactions) - Ratio of sum of CWM per CA out of the product of all CWM and all CA
- CAAI (Classified Accessor-Attribute Interactions) - Ratio of sum of CRM per CA out of the product of all CRM and all CA

# Inter-class
- CC (Critical Classes) - Number of **critical** classes. Number of classes with at least one **classified** components (CA, ICA or CM)
- ICC (Indirect Critical Classes) - Given a CA, it is the number of classes (excluding its host) that indirectly uses it, such as with direct access or method calls.

## Coupling
- ICCC (ICC Coupling) - Ratio of sum of ICC per CA out of the product of all classes and all CA (not counting the host)

## Extensibility
- CCE (CC Extensibility) - Ratio of non-final CC out of all CC
- CME (CM Extensibility) - Ratio of non-final CM out of all CM

## Inheritance
- CSCR (Critical Super Classes Ratio) - Given a class, it is the number of critical superclasses out of all its superclassess

## Size
- CCR (CC Ratio) - Ratio of CC out of all classes
- SCCR (Serialized CCR) - Ratio of Serializable CC out of CC

# Open issues?
- Exclude empty and inherited methods
- What to do with method that both read and writes?
- Should we give a "degree" of criticality?
- There are other metrics that I ignored for now
