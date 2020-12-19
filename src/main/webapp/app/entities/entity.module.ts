import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'alive-message',
        loadChildren: () => import('./alive-message/alive-message.module').then(m => m.AliveServiceAliveMessageModule),
      },
      {
        path: 'register-message',
        loadChildren: () => import('./register-message/register-message.module').then(m => m.AliveServiceRegisterMessageModule),
      },
      {
        path: 'observe',
        loadChildren: () => import('./observe/observe.module').then(m => m.AliveServiceObserveModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class AliveServiceEntityModule {}
