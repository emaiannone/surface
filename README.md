# Class-level Metrics

## Basic
- [x] CA (Classified Attributes) - Number of **classified** attributes of a class, computed through heuristics (pattern matching, e.g. password, token, etc.)
- [ ] ICA (Indirectly Classified Attributes) - Number of attributes of a class whose value is influenced (data-flow) by a **classified** attribute.
- [x] CM (Classified Methods) - Number of **classified** methods of a class.
  - [x] A method is **classified** if it uses (read/write) **classified** attributes 
  - [ ] Using heuristics on its name (pattern matching, e.g., validatePassword, generateToken).
  - Distinction between: 
    - [ ] CWM (Classified Write Method) - Number of CM that writes CA.
    - [ ] CRM (Classified Read Method) - Number of CM that reads CA.

## Encapsulation
- [x] CIVA (Classified Instance Variables Accessibility) - Ratio of non-private non-static CA out of the total of CA.
- [x] CCVA (Classified Class Variables Accessibility) - Ratio of non-private static CA out of the total of CA.
- [x] CMA (Classified Method Accessibility) - Ratio of non-private CM out of the total of CM.
- [x] RP (Reflection Package) - true/false use of java.reflection

## Cohesion
- [x] CMR (CM Ratio) - Ratio of CM out of number of total methods
  - [ ] CWMR (CWM Ratio) - Ratio of CWM out of total CM
  - [ ] CRMR (CRM Ratio) - Ratio of CRM out of total CM
- [x] CAI (Classified Attribute Interactions) - Ratio of sum of CM per CA out of the product of all CM and all CA <img src="https://render.githubusercontent.com/render/math?math=\frac{\sum_{a \in CA} |CM(a)|}{|CM| \cdot |CA|}">
  - [ ] CMAI (Classified Mutator-Attribute Interactions) - Ratio of sum of CWM per CA out of the product of all CWM and all CA
  - [ ] CAAI (Classified Accessor-Attribute Interactions) - Ratio of sum of CRM per CA out of the product of all CRM and all CA

# Project-level Metrics
- [x] CC (Critical Classes) - Number of **critical** classes. Number of classes with at least one **classified** components (CA, ICA or CM)
- [x] CCR (CC Ratio) - Ratio of CC out of all classes
- [x] SCCR (Serializable CCR) - Ratio of Serializable CC out of CC
- [ ] ICC (Indirect Critical Classes) - Given a CA, it is the number of classes (excluding its host) that indirectly uses it, such as with direct access or method calls.

## Coupling
- [ ] ICCC (ICC Coupling) - Ratio of sum of ICC per CA out of the product of all classes and all CA (not counting the host)

## Extensibility
- [x] CCE (CC Extensibility) - Ratio of non-final CC out of all CC
- [x] CME (CM Extensibility) - Ratio of non-final CM out of all CM (union)

## Inheritance
- [x] CSCR (Critical Super Classes Ratios) - For each class, it is the ratio of the number of critical superclasses out of all its superclasses

# Assumptions and Open Issues
- [x] Exclude empty and inherited methods
- [ ] How many times should we count a method that both read and writes?
- [ ] Classified methods and critical classes are boolean concepts. Should we give them a ratio?
- [ ] The papers talk about other measures that we are ignoring for not. What to do with them?

# Keywords
- name and code regex seems to be useless