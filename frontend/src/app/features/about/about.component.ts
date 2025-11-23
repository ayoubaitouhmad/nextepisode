import {Component} from '@angular/core';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [],
  template: `
    <main class="mx-auto max-w-5xl px-4 py-12 text-slate-200">
      <!-- Hero -->
      <header class="mb-10">
        <h1 class="text-3xl font-bold">À propos de <span class="text-indigo-400">NextEpisode</span></h1>
        <p class="mt-3 max-w-3xl text-slate-400">
          NextEpisode vous aide à décider plus rapidement quoi regarder.
          Il regroupe les catalogues des principales plateformes de streaming, applique des filtres intelligents
          et propose des suggestions personnalisées en fonction de vos préférences et abonnements.
        </p>
      </header>

      <!-- Purpose -->
      <section class="mb-12">
        <h2 class="text-xl font-semibold mb-3">Objectif</h2>
        <div class="rounded-lg border border-slate-700 bg-slate-900/40 p-5">
          <ul class="space-y-2 list-disc ms-5">
            <li>Unifier films et séries de plusieurs fournisseurs en un seul endroit.</li>
            <li>Faciliter le choix grâce à des filtres clairs : pays, plateformes, genres, langue, casting, note, etc.</li>
            <li>Proposer des « sélections du soir » personnalisées et des listes intelligentes : Favoris, Vu, À voir plus tard.</li>
            <li>Gagner du temps et réduire la fatigue décisionnelle — moins de temps à chercher, plus de temps à regarder.</li>
          </ul>
        </div>
      </section>

      <!-- Developers -->
      <section class="mb-12">
        <h2 class="text-xl font-semibold mb-3">Développeurs</h2>

        <div class="grid gap-4 sm:grid-cols-2">
          <!-- Saad -->
          <article class="rounded-lg border border-slate-700 bg-slate-900/40 p-5">
            <h3 class="text-lg font-semibold">ENNEJJARI Saad</h3>
            <p class="mt-2 text-sm text-slate-400">
              Développement full-stack, conception d’API, modélisation de bases de données et intégration.
              Axé sur une architecture propre et la performance.
            </p>
          </article>

          <!-- Ayoub -->
          <article class="rounded-lg border border-slate-700 bg-slate-900/40 p-5">
            <h3 class="text-lg font-semibold">AIT OUHMAD Ayoub</h3>
            <p class="mt-2 text-sm text-slate-400">
              Ingénierie front-end (Angular + Tailwind), UX et liaison des données avec le back-end.
              Axé sur une interface accessible, rapide et élégante.
            </p>
          </article>
        </div>
      </section>

      <!-- TMDB -->
      <section class="mb-12">
        <h2 class="text-xl font-semibold mb-3">Rôle de l’API TMDB</h2>
        <div class="rounded-lg border border-slate-700 bg-slate-900/40 p-5">
          <p class="text-slate-300">
            NextEpisode utilise l’API TMDB pour récupérer les métadonnées des films et séries (titres, affiches, notes),
            la disponibilité par pays et fournisseur, ainsi que des listes de référence comme les genres et les langues.
            Nous mettons en cache les ressources stables et respectons les limites et directives d’utilisation
            afin d’offrir une expérience rapide et fiable.
          </p>
          <p class="mt-3 text-sm text-slate-400">
            NextEpisode ne diffuse pas de contenu ; il vous aide à découvrir où regarder légalement sur vos services existants.
          </p>
        </div>
      </section>

      <!-- Legal -->
      <section class="mb-4">
        <h2 class="text-xl font-semibold mb-3">Mentions légales & Avertissement</h2>
        <p class="rounded-lg border border-slate-700 bg-slate-900/40 p-5 text-sm text-slate-300">
          Ce produit utilise l’API TMDB mais n’est ni approuvé ni certifié par TMDB.
        </p>
      </section>

      <!-- Optional contact / links -->
      <!--
      <footer class="mt-10 text-sm text-slate-500">
        <p>Des questions ou des retours ? <a class="underline" href="mailto:contact@nextepisode.app">contact@nextepisode.app</a></p>
      </footer>
      -->
    </main>

  `,
  styles: [`
    .about-container {
      background-color: #171717;
      min-height: 100vh;
      color: white;
      padding: 2rem;
    }

    .about-content {
      max-width: 800px;
      margin: 0 auto;
      padding: 2rem;
      background-color: #111111;
      border-radius: 1rem;
      border: 1px solid #333333;
    }

    .about-title {
      font-size: 2.5rem;
      font-weight: 700;
      color: white;
      margin: 0 0 2rem 0;
      text-align: center;
    }

    .about-section {
      margin-bottom: 2.5rem;
    }

    .about-section:last-child {
      margin-bottom: 0;
    }

    .about-description {
      font-size: 1.125rem;
      line-height: 1.6;
      color: #e5e7eb;
      margin: 0;
    }

    .section-title {
      font-size: 1.5rem;
      font-weight: 600;
      color: #3b82f6;
      margin: 0 0 1rem 0;
    }

    .developer-name {
      font-size: 1.25rem;
      font-weight: 500;
      color: white;
      margin: 0;
    }

    .contact-text {
      font-size: 1rem;
      line-height: 1.6;
      color: #e5e7eb;
      margin: 0;
    }

    .contact-link {
      color: #3b82f6;
      text-decoration: none;
      font-weight: 500;
    }

    .contact-link:hover {
      text-decoration: underline;
    }

    .faq-item {
      margin-bottom: 2rem;
    }

    .faq-item:last-child {
      margin-bottom: 0;
    }

    .faq-question {
      font-size: 1.125rem;
      font-weight: 600;
      color: white;
      margin: 0 0 0.75rem 0;
    }

    .faq-answer {
      font-size: 1rem;
      line-height: 1.6;
      color: #d1d5db;
      margin: 0;
    }

    .api-link {
      color: #3b82f6;
      text-decoration: none;
      font-weight: 500;
    }

    .api-link:hover {
      text-decoration: underline;
    }

    .attribution-text {
      font-size: 1rem;
      line-height: 1.6;
      color: #9ca3af;
      margin: 0;
      font-style: italic;
    }

    /* Responsive Design */
    @media (max-width: 768px) {
      .about-container {
        padding: 1rem;
      }

      .about-content {
        padding: 1.5rem;
      }

      .about-title {
        font-size: 2rem;
      }

      .section-title {
        font-size: 1.25rem;
      }

      .about-description {
        font-size: 1rem;
      }
    }

    @media (max-width: 480px) {
      .about-content {
        padding: 1rem;
      }

      .about-title {
        font-size: 1.75rem;
      }
    }
  `]
})
export class AboutComponent {
  constructor() {
  }
}
