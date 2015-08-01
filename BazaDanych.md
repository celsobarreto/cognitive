SpecialPages
  * id: string
  * title: string
  * content: text

News
  * id: int
  * date: date
  * title: string
  * content: text

User
  * id: int
  * is\_scientist: bool
  * is\_entrepreneur: bool
  * password\_hash: string
  * email: string  -- pełni także funkcję loginu
  * is\_accepted: bool
  * is\_activated: bool
  * competences: text
  * is\_admin: bool
  * allowed\_IPs: text
  * full\_name -- imię, nazwisko i ew. tytuł naukowy

Setting
  * id: string
  * value: text

ScienceDomain
  * id: int
  * name: string
  * description: text
  * is\_default: bool

Publication
  * id
  * title
  * keywords
  * authors
  * link
  * year
  * references
  * journal
  * volume
  * pages

Offer
  * id
  * entrepreneur\_id
  * title
  * content
  * date
  * type [job|service]

(User n:m ScienceDomain)
(User n:m Publication)
(User n:m News)